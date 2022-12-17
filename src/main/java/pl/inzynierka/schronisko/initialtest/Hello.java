package pl.inzynierka.schronisko.initialtest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;

@AllArgsConstructor
@Builder
public class Hello {
    @Getter
    @Id
    private String id;
    @Getter
    private String message;
}
