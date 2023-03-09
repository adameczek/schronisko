package pl.inzynierka.schronisko;

import java.util.List;
import pl.inzynierka.schronisko.user.*;

public class SetUp {
  public static final String PASSWORD = "Dupk@1234";

  public static class UserSetup {
    public static void initUsersInDb(UserService userService) throws UserServiceException {
      for (User user : getBasicUserList()) {
        User createdUser = userService.createUser(user);
        System.out.println(createdUser);
      }
    }

    public static List<User> getBasicUserList() {
      return List.of(
          createModerator("moderator", "moderator@email.com"),
          createAdmin("admin", "admin@email.com"),
          createUser("userek", "user@email.com"));
    }

    public static User createModerator(String username, String email) {
      return User.builder()
          .roles(List.of(Role.MODERATOR, Role.USER))
          .password(PASSWORD)
          .username(username)
          .email(email)
          .id("1")
          .firstname("Robert")
          .lastname("Kubica")
          .active(true)
          .build();
    }

    public static User createAdmin(String username, String email) {
      return User.builder()
          .roles(List.of(Role.ADMIN, Role.MODERATOR, Role.USER))
          .password(PASSWORD)
          .username(username)
          .email(email)
          .lastname("Kubica")
          .firstname("Robert")
          .active(true)
          .build();
    }

    public static User createUser(String username, String email) {
      return User.builder()
          .username(username)
          .email(email)
          .password(PASSWORD)
          .roles(List.of(Role.USER))
          .firstname("Robert")
          .lastname("Kubica")
          .active(true)
          .build();
    }
  }
}
