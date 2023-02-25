package pl.inzynierka.schronisko;

import java.time.LocalDateTime;

public record ErrorResponse(LocalDateTime date, String error) {
    public static ErrorResponse now(String error) {
        return new ErrorResponse(LocalDateTime.now(), error);
    }
}
