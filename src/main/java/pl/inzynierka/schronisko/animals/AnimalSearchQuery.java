package pl.inzynierka.schronisko.animals;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.List;

@Builder
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AnimalSearchQuery {
    @Schema(description = "Tags that searched animals should contain")
    private List<String> tags;
    @Schema(description = "Types that searched animals should contain")
    private List<String> types;
    @Schema(description = "name of searched animal")
    private String name;
    @Schema(description = "Animals created by certain user, you can provide here ")
    private String createdBy;
}
