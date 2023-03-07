package pl.inzynierka.schronisko.configuration;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import pl.inzynierka.schronisko.TestUtils;
import pl.inzynierka.schronisko.configurations.security.UserPrincipal;

public class TestUserDetailsService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws
            UsernameNotFoundException {
        UserPrincipal userPrincipal;
        if (username.equals("moderator")) {
            userPrincipal = new UserPrincipal(TestUtils.moderator);
        }
        throw new UsernameNotFoundException("user not found!");

    }
}
