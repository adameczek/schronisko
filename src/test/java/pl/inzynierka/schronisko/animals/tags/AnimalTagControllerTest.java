package pl.inzynierka.schronisko.animals.tags;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;
import pl.inzynierka.schronisko.SchroniskoApplication;
import pl.inzynierka.schronisko.authentication.AuthRequest;
import pl.inzynierka.schronisko.authentication.AuthResponse;
import pl.inzynierka.schronisko.authentication.AuthenticationService;
import pl.inzynierka.schronisko.configuration.TestConfiguration;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        classes = SchroniskoApplication.class
)
@Import(TestConfiguration.class)
@AutoConfigureDataMongo
class AnimalTagControllerTest {

    WebTestClient client;
    String token;
    @Autowired
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp(ApplicationContext applicationContext) {
        AuthResponse login = authenticationService.login(new AuthRequest(
                "moderator",
                "1234"));
        token = login.getToken();

        client = WebTestClient.bindToApplicationContext(applicationContext)
                .configureClient()
                .defaultHeaders(httpHeaders -> httpHeaders.setBearerAuth(token))
                .build();
    }

    @Test
    void getAllTags() throws Exception {
        WebTestClient.ResponseSpec exchange = client.get()
                .uri("/animals/tags").exchange();

        exchange.expectStatus().is2xxSuccessful();
    }
}