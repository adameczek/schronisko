package pl.inzynierka.schronisko.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebFluxSecurity
public class WebSecurityConfiguration {
	/*@Bean
	public MapReactiveUserDetailsService userDetailsService() {
		UserDetails user = User.withDefaultPasswordEncoder().username("user").password("user").roles("USER").build();
		return new MapReactiveUserDetailsService(user);
	}*/

	@Bean
	SecurityWebFilterChain webHttpSecurity(ServerHttpSecurity http) {
		http.authorizeExchange((exchanges) -> exchanges.anyExchange().permitAll()).httpBasic(withDefaults());
		return http.build();
	}

}
