package pl.inzynierka.schronisko.authentication;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
final class AuthRequest {
	private String username;
	private String password;
}
