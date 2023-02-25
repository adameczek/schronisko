package pl.inzynierka.schronisko.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import pl.inzynierka.schronisko.ErrorResponse;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)

public class UserController {
	private final UserService userService;

	@GetMapping
	@PreAuthorize("hasAuthority('USER')")
	ResponseEntity<Page<User>> getUsers(final Pageable pageable) {
		return ResponseEntity.ok(this.userService.getUsers(pageable));
	}

	@GetMapping("/me")
	@PreAuthorize("hasAuthority('USER')")
	ResponseEntity<User> getLoggedUser(final Principal userPrincipal) {
		return this.getUser(userPrincipal.getName());
	}

	@GetMapping("/{username}")
	@PreAuthorize("hasAuthority('USER')")
	ResponseEntity<User> getUser(@PathVariable final String username) {
		final Optional<User> user = this.userService.findByUsername(username);

		return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PutMapping("/{username}")
	@PreAuthorize("hasAuthority('USER')")
	ResponseEntity<User> updateUser(@PathVariable final String username, @RequestBody final User user) throws UserServiceException {
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		final User userDetails = (User) authentication.getPrincipal();

		return ResponseEntity.ok(this.userService.updateUser(username, user, userDetails));
	}


	@SneakyThrows
	@PostMapping
	ResponseEntity<User> createUser(@Valid @RequestBody final User user) {
		return ResponseEntity.ok(this.userService.createUser(user));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	ResponseEntity<Map<String, String>> onNonValidRequest(final MethodArgumentNotValidException e, final WebRequest request) {
		final Map<String, String> errors = new HashMap<>();
		e.getBindingResult().getAllErrors().forEach((error) -> {
			final String fieldName = ((FieldError) error).getField();
			final String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		return ResponseEntity.badRequest().body(errors);
	}

	@ExceptionHandler(UserServiceException.class)
	ResponseEntity<ErrorResponse> userServiceError(final UserServiceException e, final WebRequest request) {
		return ResponseEntity.badRequest().body(new ErrorResponse(LocalDateTime.now(), e.getMessage()));
	}

	@ExceptionHandler(Exception.class)
	ResponseEntity<ErrorResponse> handleAnyException(final Exception e, final WebRequest request) {
		return ResponseEntity.badRequest().body(ErrorResponse.now(e.getMessage()));
	}
}
