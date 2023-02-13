package pl.inzynierka.schronisko.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor(onConstructor = @__({@JsonCreator}))
@ToString
public class Username {
	private String username;
}
