package pl.inzynierka.schronisko.animals.tags;

import lombok.NoArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.context.WebApplicationContext;
import pl.inzynierka.schronisko.SchroniskoApplication;
import pl.inzynierka.schronisko.SetUp;
import pl.inzynierka.schronisko.TestUtils;
import pl.inzynierka.schronisko.authentication.AuthenticationService;
import pl.inzynierka.schronisko.user.UserService;
import pl.inzynierka.schronisko.user.UserServiceException;
import reactor.core.publisher.Mono;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        classes = SchroniskoApplication.class
)
@AutoConfigureDataMongo
@NoArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AnimalTagControllerTest {
    @Autowired
    WebTestClient client;
    String moderatorToken;
    String userToken;
    @Autowired
    UserService userService;
    @Autowired
    private AuthenticationService authenticationService;
    
    @BeforeAll
    void setUp(WebApplicationContext applicationContext) throws UserServiceException {
        SetUp.UserSetup.initUsersInDb(userService);
        moderatorToken = TestUtils.loginAndGetToken(authenticationService, "moderator");
        userToken = TestUtils.loginAndGetToken(authenticationService, "userek");
    }
    
    @Test
    @Order(1)
    void saveTag() {
        String tagValue = "test";
        client.post()
              .uri("/animals/tags")
              .body(Mono.just(AnimalTag.builder().value(tagValue).build()), AnimalTag.class)
              .headers(httpHeaders -> httpHeaders.setBearerAuth(moderatorToken))
              .exchange()
              .expectStatus()
              .is2xxSuccessful()
              .expectBody()
              .jsonPath("$.value")
              .isEqualTo(tagValue);
    }
    
    @Test
    @Order(2)
    void tryToCreateTagAsUser() {
        client.post().uri("/animals/tags").body(Mono.just(AnimalTag.builder().value("nowy tag od usera").build()),
                                                AnimalTag.class).headers(httpHeaders -> httpHeaders.setBearerAuth(
                userToken)).exchange().expectStatus().is4xxClientError();
    }
    
    @Test
    @Order(3)
    void tryToCreateExistingTag() {
        String tagValue = "test";
        client.post()
              .uri("/animals/tags")
              .body(Mono.just(AnimalTag.builder().value(tagValue).build()), AnimalTag.class)
              .headers(httpHeaders -> httpHeaders.setBearerAuth(moderatorToken))
              .exchange()
              .expectStatus()
              .is4xxClientError()
              .expectBody()
              .jsonPath("$.date")
              .exists()
              .jsonPath("$.error");
    }
    
    @Test
    @Order(4)
    void getAllTags() {
        client.get()
              .uri("/animals/tags")
              .headers(httpHeaders -> httpHeaders.setBearerAuth(moderatorToken))
              .exchange()
              .expectStatus()
              .is2xxSuccessful()
              .expectBody()
              .jsonPath("$.totalElements")
              .isEqualTo(1)
              .jsonPath("$.content[0].value")
              .isEqualTo("test");
    }
}
