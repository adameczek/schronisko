package pl.inzynierka.schronisko.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import pl.inzynierka.schronisko.common.ErrorResponse;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Users", description = "provides user data")
public class UserController {
    private final UserService userService;
    private final ModelMapper modelMapper;

    @GetMapping
    @PreAuthorize("hasAuthority('USER')")
    @Operation(
            summary = "Gets users",
            description = "lists paginated users list",
            tags = {"users"}
    )
    ResponseEntity<Page<UserResponse>> getUsers(
            @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(this.userService.getUsers(pageable)
                                         .map(this::convertToResponse));
    }

    private UserResponse convertToResponse(User user) {
        return modelMapper.map(user, UserResponse.class);
    }

    @GetMapping("/me")
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "Gets authenticated user", tags = {"user"})
    ResponseEntity<UserResponse> getLoggedUser() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        final User userDetails = (User) authentication.getPrincipal();
        return getUser(userDetails.getUsername());
    }

    @GetMapping("/{username}")
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "Get user by username")
    ResponseEntity<UserResponse> getUser(@PathVariable final String username) {
        final Optional<User> user = userService.findByUsername(username);

        return user.map(this::convertToResponse).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{username}")
    @PreAuthorize("hasAuthority('USER')")
    @Operation(
            summary = "Updates user",
            description = "Updates user provided in path variable. Users without admin role cannot update other users."
    )
    ResponseEntity<UserResponse> updateUser(@PathVariable final String username,
                                            @RequestBody
                                            final UserUpdateRequest user) throws
            UserServiceException {
        final Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        final User userDetails = (User) authentication.getPrincipal();

        return ResponseEntity.ok(convertToResponse(this.userService.updateUser(
                username,
                user,
                userDetails)));
    }


    @ExceptionHandler(UserServiceException.class)
    ResponseEntity<ErrorResponse> userServiceError(final UserServiceException e,
                                                   final WebRequest request) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(LocalDateTime.now(), e.getMessage()));
    }

    @SneakyThrows
    @PostMapping
    @Operation(summary = "Creates user")
    ResponseEntity<UserResponse> createUser(
            @Valid @RequestBody final User user) {
        return ResponseEntity.ok(convertToResponse(this.userService.createUser(
                user)));
    }
}
