package pl.inzynierka.schronisko.animals;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.inzynierka.schronisko.fileupload.ImageFileResponse;
import pl.inzynierka.schronisko.user.UserResponse;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AnimalResponse {
    
    @Schema(description = "List of animal tags to help with searching")
    private List<String> tags;
    @Schema(description = "Creator of animal")
    private UserResponse createdBy;
    private long id;
    @Schema(
            description = "Type of animal",
            example = "Pies"
    )
    private String type;
    @Schema(
            description = "Race of animal",
            example = "Beagle"
    )
    private String race;
    @Schema(
            description = "Name of animal",
            example = "Burek"
    )
    private String name;
    @Schema(description = "Description of animal")
    private String description;
    @Schema(description = "Weight of animal")
    private Double weight;
    @Schema(
            description = "Creation date of animal",
            example = "2023-03-23T19:45:00.659014900"
    )
    private String created;
    @Schema(
            description = "Date of last update of animal",
            example = "2023-03-23T19:45:00.659014900"
    )
    private String updated;
    private List<ImageFileResponse> images;
    
}
