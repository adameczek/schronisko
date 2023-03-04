package pl.inzynierka.schronisko.animals.tags;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@AllArgsConstructor
public class AnimalTag {
    @Id
    @Schema(description = "id of tag")
    private String id;
    @NotNull
    @Size(min = 1, max = 50)
    @Indexed(unique = true)
    @Schema(description = "unique tag value", example = "rudy")
    private String value;
}
