package pl.inzynierka.schronisko;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Getter
public class ErrorResponse {
	private final LocalDateTime date;
	private final String error;

	public static ErrorResponse now(final String error) {
		return new ErrorResponse(LocalDateTime.now(), error);
	}
}
