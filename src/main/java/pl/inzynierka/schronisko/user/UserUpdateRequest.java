package pl.inzynierka.schronisko.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserUpdateRequest {
    private String username;
    private String password;
    private String firstname;
    private String lastname;
    private boolean active;
}
