package pl.inzynierka.schronisko.animals;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import pl.inzynierka.schronisko.animals.types.Difficulty;

import java.util.List;

@Builder
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AnimalSearchQuery {
    @Schema(description = "Tags that searched animals should contain")
    private List<String> tags;
    @Schema(description = "Types that searched animals should contain")
    private List<String> types;
    @Schema(description = "name of searched animal")
    private String name;
    @Schema(description = "color of animal")
    private String color;
    @Schema(description = "Sex of animal")
    private Sex sex;
    @Schema(description = "Difficulty of animal")
    private Difficulty difficulty;
    @Schema(
            description = "Animals created by certain user, you can provide here, it searches by email",
            example = "user@email.com"
    )
    private String createdBy;
    @Schema(
            description = "by which field it should be sorted, default it sorts by create date ascending. You "
                          + "start with '+' or '-', and then type name of field.",
            example = "+type"
    )
    private String sortBy;
}
