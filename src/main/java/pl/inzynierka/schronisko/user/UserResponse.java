package pl.inzynierka.schronisko.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserResponse {
    @Schema(description = "Unique identifier of the contact.", example = "1")
    private long id;
    @Schema(description = "Username of user.", example = "Pawulonik21")
    private String username;
    @Schema(description = "Email of user", example = "robert@kubica.pl")
    private String email;
    @Schema(description = "user firstname", example = "Mieczyslaw")
    private String firstname;
    @Schema(description = "user lastname", example = "Chrabonszczak")
    private String lastname;
    @Schema(description = "Date of account creation")
    private String joined;
    private String updated;
    private List<String> roles;
    private boolean active;
}
