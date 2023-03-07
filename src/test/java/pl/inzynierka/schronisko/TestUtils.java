package pl.inzynierka.schronisko;

import pl.inzynierka.schronisko.user.Role;
import pl.inzynierka.schronisko.user.User;

import java.util.List;

public class TestUtils {
    public static User moderator = User.builder()
            .roles(List.of(Role.MODERATOR,
                           Role.USER))
            .password("1234")
            .username("moderator")
            .id("1")
            .firstname("Robert")
            .lastname("Kubica")
            .active(true)
            .build();

}
