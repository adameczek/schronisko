package pl.inzynierka.schronisko.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Data
@Document
@Builder
@NoArgsConstructor
public class User {

	@Id
	public ObjectId id;
	@NotBlank(message = "Username can't be empty")
	@Indexed(unique = true)
	@Size(min = 1, max = 50)
	private String username;
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@NotBlank()
	@Size(min = 8, max = 100)
	private String password;
	@Indexed(unique = true)
	@NotBlank
	@Size(min = 1, max = 100)
	private String email;
	@Size(min = 1, max = 100)
	private String firstname;
	@Size(min = 1, max = 100)
	private String lastname;
	@Indexed
	private LocalDate joined;
	@Builder.Default
	private List<Role> roles = new ArrayList<>(List.of(Role.USER));
	@Builder.Default
	private boolean active = true;
}
