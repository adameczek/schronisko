package pl.inzynierka.schronisko.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetailsService;

@org.springframework.boot.test.context.TestConfiguration
public class TestConfiguration {
    @Bean
    @Primary
    public UserDetailsService userDetailsService() {
        return new TestUserDetailsService();
    }
}
