package pl.inzynierka.schronisko.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import pl.inzynierka.schronisko.shelters.models.Shelter;
import pl.inzynierka.schronisko.user.validators.ValidPassword;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@Builder
@Entity(name = "users")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Schema(
            description = "Unique identifier of the contact.",
            example = "1"
    )
    private long id;
    @Column(
            unique = true,
            nullable = true
    )
    @Size(
            min = 1,
            max = 50
    )
    @Schema(
            description = "Username of user.",
            example = "Pawulonik21"
    )
    private String username;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank
    @Column(nullable = false)
    @ValidPassword
    @Schema(
            description = "password of user",
            example = "P@$$w0rd"
    )
    private String password;
    @NotBlank
    @Size(
            min = 1,
            max = 100
    )
    @Email(message = "Email is not valid")
    @Schema(
            description = "Email of user",
            example = "robert@kubica.pl"
    )
    @Column(
            unique = true,
            nullable = false
    )
    private String email;
    @Size(
            min = 1,
            max = 100
    )
    @Schema(
            description = "user firstname",
            example = "Mieczyslaw"
    )
    private String firstname;
    @Size(
            min = 1,
            max = 100
    )
    @Schema(
            description = "user lastname",
            example = "Chrabonszczak"
    )
    private String lastname;
    @CreatedDate
    @Column(
            updatable = false,
            nullable = false
    )
    @Schema(description = "Date of account creation")
    private LocalDateTime joined;
    @LastModifiedDate
    private LocalDateTime updated;
    @Builder.Default
    @Schema(description = "Roles of user")
    private List<Role> roles = new ArrayList<>(List.of(Role.USER));
    @Builder.Default
    @Schema(description = "Is user account active")
    private boolean active = true;
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Shelter shelter;
    
    public boolean hasRoles(Role... roles) {
        return Arrays.stream(roles).allMatch(role -> getRoles().contains(role));
    }
    
    public boolean hasNoRoles(Role... roles) {
        return Arrays.stream(roles).noneMatch(role -> getRoles().contains(role));
    }
    
    public boolean hasAnyRole(Role... roles) {
        return Arrays.stream(roles).anyMatch(role -> getRoles().contains(role));
    }
}
