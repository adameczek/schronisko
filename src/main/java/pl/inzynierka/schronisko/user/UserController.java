package pl.inzynierka.schronisko.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import pl.inzynierka.schronisko.ErrorResponse;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {
	private final UserService userService;

	@SneakyThrows
	@PostMapping
	ResponseEntity<User> createUser(@Valid @RequestBody User user) {
		return ResponseEntity.ok(userService.createUser(user));
	}

	@ExceptionHandler({MethodArgumentNotValidException.class})
	ResponseEntity<Map<String, String>> onNonValidRequest(MethodArgumentNotValidException e, WebRequest request) {
		Map<String, String> errors = new HashMap<>();
		e.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		return ResponseEntity.badRequest().body(errors);
	}

	@ExceptionHandler({UserServiceException.class})
	ResponseEntity<ErrorResponse> userServiceError(UserServiceException e, WebRequest request)
	{
		return ResponseEntity.badRequest().body(new ErrorResponse(LocalDateTime.now(),e.getMessage()));
	}

	@ExceptionHandler({Exception.class})
	ResponseEntity<ErrorResponse> handleAnyException(Exception e, WebRequest request)
	{
		return ResponseEntity.badRequest().body(ErrorResponse.now(e.getMessage()));
	}
}
