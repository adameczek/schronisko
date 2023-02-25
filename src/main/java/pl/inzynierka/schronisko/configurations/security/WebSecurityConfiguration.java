package pl.inzynierka.schronisko.configurations.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import pl.inzynierka.schronisko.authentication.UserDetailsServiceImpl;
import pl.inzynierka.schronisko.configurations.security.jwt.JwtTokenAuthenticationFilter;
import pl.inzynierka.schronisko.configurations.security.jwt.JwtTokenProvider;

/**
 * <a href="https://www.devglan.com/spring-security/spring-webflux-rest-authentication">How to</a>
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class WebSecurityConfiguration {
	private final UserDetailsServiceImpl userDetailsService;
	private final JwtTokenProvider jwtTokenProvider;


	@Bean
	public AuthenticationManager customAuthenticationManager(final HttpSecurity http) throws Exception {
		final AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject
				(AuthenticationManagerBuilder.class);
		authenticationManagerBuilder.userDetailsService(this.userDetailsService)
				.passwordEncoder(this.bCryptPasswordEncoder());
		return authenticationManagerBuilder.build();
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}


	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
		// @formatter:off
		http
				.authorizeHttpRequests((authorize) -> authorize
						.requestMatchers("/login").permitAll()
						.requestMatchers(HttpMethod.POST, "/users").permitAll()
						.anyRequest().authenticated()
				)
				.csrf(AbstractHttpConfigurer::disable)
				.httpBasic(AbstractHttpConfigurer::disable)
				.addFilterBefore(new JwtTokenAuthenticationFilter(this.jwtTokenProvider),
						UsernamePasswordAuthenticationFilter.class)
				.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.exceptionHandling(c -> c.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)));

		// @formatter:on
		return http.build();
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

		final CorsConfiguration corsConfiguration = new CorsConfiguration().applyPermitDefaultValues();
		source.registerCorsConfiguration("/**", corsConfiguration);

		return source;
	}

}
