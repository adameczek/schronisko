package pl.inzynierka.schronisko.configurations.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import pl.inzynierka.schronisko.configurations.security.jwt.JwtTokenAuthenticationFilter;
import pl.inzynierka.schronisko.configurations.security.jwt.JwtTokenProvider;
import pl.inzynierka.schronisko.user.UserRepository;
import reactor.core.publisher.Mono;


/**
 * <a href="https://www.devglan.com/spring-security/spring-webflux-rest-authentication">How to</a>
 */
@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class WebSecurityConfiguration {
	@Bean
	SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http,
	                                            JwtTokenProvider tokenProvider,
	                                            ReactiveAuthenticationManager reactiveAuthenticationManager) {
		final String PATH_POSTS = "/posts/**";

		return http.csrf(ServerHttpSecurity.CsrfSpec::disable)
				.httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
				.authenticationManager(reactiveAuthenticationManager)
				.securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
				.authorizeExchange(it -> it
						.pathMatchers(HttpMethod.GET, PATH_POSTS).permitAll()
						.pathMatchers(HttpMethod.DELETE, PATH_POSTS).hasRole("ADMIN")
						.pathMatchers(PATH_POSTS).authenticated()
						.pathMatchers("/login").permitAll()
						.pathMatchers("/me").authenticated()
						.pathMatchers("/users/{user}/**").access(this::currentUserMatchesPath)
						.anyExchange().permitAll()
				)
				.addFilterAt(new JwtTokenAuthenticationFilter(tokenProvider), SecurityWebFiltersOrder.HTTP_BASIC)
				.build();


	}

	private Mono<AuthorizationDecision> currentUserMatchesPath(Mono<Authentication> authentication,
	                                                           AuthorizationContext context) {

		return authentication
				.map(a -> context.getVariables().get("user").equals(a.getName()))
				.map(AuthorizationDecision::new);

	}

	@Bean
	public ReactiveUserDetailsService userDetailsService(UserRepository users) {

		return username -> users.findByUsername(username)
				.map(u -> User
						.withUsername(u.getUsername()).password(u.getPassword())
						.authorities(u.getRoles().toArray(new String[0]))
						.accountExpired(!u.isActive())
						.credentialsExpired(!u.isActive())
						.disabled(!u.isActive())
						.accountLocked(!u.isActive())
						.build()
				);
	}

	@Bean
	public ReactiveAuthenticationManager reactiveAuthenticationManager(ReactiveUserDetailsService userDetailsService,
	                                                                   PasswordEncoder passwordEncoder) {
		var authenticationManager = new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
		authenticationManager.setPasswordEncoder(passwordEncoder);

		return authenticationManager;
	}
}
