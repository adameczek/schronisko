package pl.inzynierka.schronisko.animals;


import lombok.NoArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.context.WebApplicationContext;
import pl.inzynierka.schronisko.JsonRequestBodyLoader;
import pl.inzynierka.schronisko.SchroniskoApplication;
import pl.inzynierka.schronisko.SetUp;
import pl.inzynierka.schronisko.TestUtils;
import pl.inzynierka.schronisko.authentication.AuthenticationService;
import pl.inzynierka.schronisko.user.UserService;
import pl.inzynierka.schronisko.user.UserServiceException;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        classes = SchroniskoApplication.class
)
@AutoConfigureDataMongo
@NoArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AnimalsControllerTest {
    @Autowired
    WebTestClient client;
    @Autowired
    AnimalsTestDBInitializer dbInitializer;
    @Autowired
    JsonRequestBodyLoader loader;
    String moderatorToken;
    String userToken;

    @Autowired
    UserService userService;
    @Autowired
    AuthenticationService authenticationService;

    @BeforeAll
    void setUp(WebApplicationContext applicationContext) throws
            UserServiceException {
        SetUp.UserSetup.initUsersInDb(userService);
        moderatorToken = TestUtils.loginAndGetToken(authenticationService,
                                                    "moderator");
        userToken = TestUtils.loginAndGetToken(authenticationService, "userek");
        dbInitializer.initDB();
    }

    @Test
    @Order(1)
    void createAnimal() throws IOException {
        String requestBody = loader.getRequestFromFile("default",
                                                       "animalRequests");

        client.post()
                .uri(uriBuilder -> uriBuilder.path("/animals").build())
                .headers(httpHeaders -> httpHeaders.setContentType(MediaType.APPLICATION_JSON))
                .headers(httpHeaders -> httpHeaders.setBearerAuth(moderatorToken))
                .body(Mono.just(requestBody), String.class)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .jsonPath("$.name")
                .isEqualTo("Mruczuś")
                .jsonPath("$.tags[0].value")
                .value(s -> {
                    assertTrue(List.of("rudy", "ruchliwy").contains(s));
                }, String.class);
    }


}
