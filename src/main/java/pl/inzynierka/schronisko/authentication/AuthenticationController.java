package pl.inzynierka.schronisko.authentication;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Authentication", description = "Provides authentication")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Operation(summary = "Generates api token to use")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Authorization successful",
                            content = {@Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AuthResponse.class)
                            )}
                    )
                    ,
                    @ApiResponse(
                            responseCode = "400",
                            description = "Authorization failed",
                            content = {@Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )}
                    )}
    )
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody AuthRequest authRequest) {
        return authenticationService.login(authRequest);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> errorRes(Exception ex,
                                                  WebRequest request) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(LocalDateTime.now(), ex.getMessage()));
    }
}
