package pl.inzynierka.schronisko.authentication;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import pl.inzynierka.schronisko.ErrorResponse;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {

	private final AuthenticationService authenticationService;

	@PostMapping("/login")
	public ResponseEntity<AuthResponse> login(@RequestBody final AuthRequest authRequest) {
		return this.authenticationService.login(authRequest);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> errorRes(final Exception ex, final WebRequest request) {
		return ResponseEntity.badRequest().body(new ErrorResponse(LocalDateTime.now(), ex.getMessage()));
	}
}
