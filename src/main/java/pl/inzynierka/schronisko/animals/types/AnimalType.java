package pl.inzynierka.schronisko.animals.types;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@AllArgsConstructor
@Builder
public class AnimalType {

    @Id
    @Schema(description = "id of tag")
    private String id;
    @NotNull
    @Size(min = 1, max = 100)
    @Indexed(unique = true)
    @Schema(description = "Type of animal", example = "pies")
    private String value;
}
