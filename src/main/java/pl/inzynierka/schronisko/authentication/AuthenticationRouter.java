package pl.inzynierka.schronisko.authentication;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;


@Configuration(proxyBeanMethods = false)
public class AuthenticationRouter {
	AuthenticationHandler authenticationHandler;

	@Bean
	public RouterFunction<ServerResponse> route(AuthenticationHandler authenticationHandler) {
		return RouterFunctions
				.route(POST("/login").and(accept(MediaType.APPLICATION_JSON)), authenticationHandler::login);

	}
}
