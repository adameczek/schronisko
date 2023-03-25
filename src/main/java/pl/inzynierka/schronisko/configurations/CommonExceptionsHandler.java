package pl.inzynierka.schronisko.configurations;

import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import pl.inzynierka.schronisko.animals.InsufficentUserRoleException;
import pl.inzynierka.schronisko.common.ErrorResponse;
import pl.inzynierka.schronisko.common.SchroniskoException;

@ControllerAdvice
@Slf4j
public class CommonExceptionsHandler {
  @ExceptionHandler({Exception.class})
  public ResponseEntity<ErrorResponse> handleUnknownException(Exception e, WebRequest request) {
    log.error("Unknown exception occured!");
    e.printStackTrace();

    return ResponseEntity.internalServerError()
        .body(ErrorResponse.now("Unknown exception occured, please contact administrators!"));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  ResponseEntity<Map<String, String>> onNonValidRequest(
      final MethodArgumentNotValidException e, final WebRequest request) {
    log.error(
        "Method argument not valid exception happaned!\nRequest info: {}",
        request.getDescription(true));

    Map<String, String> errors = new HashMap<>();
    e.getBindingResult()
        .getAllErrors()
        .forEach(
            (error) -> {
              final String fieldName = ((FieldError) error).getField();
              final String errorMessage = error.getDefaultMessage();
              errors.put(fieldName, errorMessage);
            });
    return ResponseEntity.badRequest().body(errors);
  }

  @ExceptionHandler(InsufficentUserRoleException.class)
  ResponseEntity<ErrorResponse> onInsufficientUserRoles(Exception e, WebRequest request) {
    log.error(
        "User with insufficient roles tried to make request: {}", request.getDescription(true));

    return ResponseEntity.badRequest().body(ErrorResponse.now(e.getMessage()));
  }

  @ExceptionHandler(AccessDeniedException.class)
  ResponseEntity<ErrorResponse> onAccessDenied(Exception e, WebRequest request) {
    log.error("Access denied for request: {}", request.getDescription(true));

    return ResponseEntity.status(403).body(ErrorResponse.now("Brak dostępu"));
  }

  @ExceptionHandler(SchroniskoException.class)
  ResponseEntity<ErrorResponse> onAppException(Exception e, WebRequest request) {
    log.error(
        "Exception occurred: {} for request: {}", e.getMessage(), request.getDescription(true));

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.now(e.getMessage()));
  }
}
