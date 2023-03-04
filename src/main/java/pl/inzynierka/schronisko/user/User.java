package pl.inzynierka.schronisko.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import pl.inzynierka.schronisko.user.validators.ValidPassword;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@Data
@Document
@Builder
@NoArgsConstructor
public class User {

    @Id
    @Schema(description = "Unique identifier of the contact.", example = "1")
    public String id;
    @NotBlank(message = "Username can't be empty")
    @Indexed(unique = true)
    @Size(min = 1, max = 50)
    @Schema(description = "Username of user.", example = "Pawulonik21")
    private String username;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank
    @ValidPassword
    @Schema(description = "password of user", example = "P@$$w0rd")
    private String password;
    @Indexed(unique = true)
    @NotBlank
    @Size(min = 1, max = 100)
    @Email(message = "Email is not valid")
    @Schema(description = "Email of user", example = "robert@kubica.pl")
    private String email;
    @Size(min = 1, max = 100)
    @Schema(description = "user firstname", example = "Mieczyslaw")
    private String firstname;
    @Size(min = 1, max = 100)
    @Schema(description = "user lastname", example = "Chrabonszczak")
    private String lastname;
    @Indexed
    @Schema(description = "Date of account creation")
    private LocalDate joined;
    @Builder.Default
    @Schema(description = "Roles of user")
    private List<Role> roles = new ArrayList<>(List.of(Role.USER));
    @Builder.Default
    @Schema(description = "Is user account active")
    private boolean active = true;

    public boolean hasRoles(Role... roles) {
        return Arrays.stream(roles).allMatch(role -> getRoles().contains(role));
    }

    public boolean hasNoRoles(Role... roles) {
        return Arrays.stream(roles)
                .noneMatch(role -> getRoles().contains(role));
    }

    public boolean hasAnyRole(Role... roles) {
        return Arrays.stream(roles).anyMatch(role -> getRoles().contains(role));
    }
}
