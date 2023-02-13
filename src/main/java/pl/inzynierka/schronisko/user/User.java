package pl.inzynierka.schronisko.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
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
public class User {

	@Id
	private String username;
	@JsonIgnore
	private String password;
	@Indexed(unique = true)
	private String email;
	private String firstname;
	private String lastname;
	@Indexed
	private LocalDate joined;
	@Builder.Default
	private List<Roles> roles = new ArrayList<>(List.of(Roles.USER));
	@Builder.Default
	private boolean active = true;
}
