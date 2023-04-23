package pl.inzynierka.schronisko;

import pl.inzynierka.schronisko.authentication.AuthRequest;
import pl.inzynierka.schronisko.authentication.AuthResponse;
import pl.inzynierka.schronisko.authentication.AuthenticationService;

public class TestUtils {
    public static String loginAndGetToken(AuthenticationService authenticationService, String username) {
        AuthResponse login = authenticationService.login(new AuthRequest(username, SetUp.PASSWORD));
        assert login.getToken() != null;
        return login.getToken();
    }
}
