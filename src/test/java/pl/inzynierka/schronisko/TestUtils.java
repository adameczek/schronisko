package pl.inzynierka.schronisko;

import java.util.List;
import pl.inzynierka.schronisko.authentication.AuthRequest;
import pl.inzynierka.schronisko.authentication.AuthResponse;
import pl.inzynierka.schronisko.authentication.AuthenticationService;
import pl.inzynierka.schronisko.user.Role;
import pl.inzynierka.schronisko.user.User;

public class TestUtils {
  public static User moderator =
      User.builder()
          .roles(List.of(Role.MODERATOR, Role.USER))
          .password("1234")
          .username("moderator")
          .id("1")
          .firstname("Robert")
          .lastname("Kubica")
          .active(true)
          .build();

  public static String loginAndGetToken(
      AuthenticationService authenticationService, String username) {
    AuthResponse login = authenticationService.login(new AuthRequest(username, SetUp.PASSWORD));
    assert login.getToken() != null;
    return login.getToken();
  }
}
