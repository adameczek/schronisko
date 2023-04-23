package pl.inzynierka.schronisko.animals;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
public class AnimalRequest {
    
    @Schema(description = "List of animal tags to help with searching")
    private List<String> tags;
    @NotNull
    @Schema(
            description = "Type of animal",
            example = "kot"
    )
    private String type;
    @Schema(
            description = "Race of animal",
            example = "Brytyjczyk"
    )
    @Nullable
    private String race;
    @NotNull
    @Size(
            min = 1,
            max = 50
    )
    @Schema(
            description = "Name of animal, cannot be null",
            example = "Peja"
    )
    private String name;
    @Size(
            min = 0,
            max = 1000
    )
    @Schema(
            description = "Description of animal",
            example = "mily, gryzie tylko dzieci"
    )
    private String description;
    @NotNull
    @Schema(
            description = "Name of animal shelter",
            example = "Pieskowo"
    )
    private String shelter;
}
