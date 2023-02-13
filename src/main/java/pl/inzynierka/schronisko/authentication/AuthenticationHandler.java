package pl.inzynierka.schronisko.authentication;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import pl.inzynierka.schronisko.ErrorResponse;
import pl.inzynierka.schronisko.configurations.security.jwt.JwtTokenProvider;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class AuthenticationHandler {
	private final JwtTokenProvider jwtTokenProvider;
	private final ReactiveAuthenticationManager reactiveAuthenticationManager;

	public Mono<ServerResponse> login(ServerRequest serverRequest) {
		return serverRequest.bodyToMono(AuthRequest.class).flatMap(
						login -> this.reactiveAuthenticationManager.authenticate(
								new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword())))
				.map(this.jwtTokenProvider::createToken).flatMap(
						jwt -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
								.header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
								.body(BodyInserters.fromValue(new AuthResponse(jwt)))).onErrorResume(
						throwable -> ServerResponse.badRequest().contentType(MediaType.APPLICATION_JSON)
								.bodyValue(ErrorResponse.now(throwable.getMessage())));

	}
}
