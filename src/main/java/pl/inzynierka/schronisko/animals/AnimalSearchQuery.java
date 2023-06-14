package pl.inzynierka.schronisko.animals;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

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
    @Schema(description = "Types that searched animals should not contain")
    private List<String> notInTypes;
    @Schema(description = "name of searched animal")
    private String name;
    @Schema(
            description = "Animals created by certain user, you can provide here, it searches by email",
            example = "user@email.com"
    )
    private String createdBy;
    @Schema(
            description = "Animal sex"
    )
    @Nullable
    private List<Sex> sex;
    @Nullable
    private List<AnimalAge> age;
    @Nullable
    private List<AnimalSize> size;
    @Schema(
            description = "by which field it should be sorted, default it sorts by create date ascending. You "
                          + "start with '+' or '-', and then type name of field.",
            example = "+type"
    )
    private String sortBy;
    
    protected enum AnimalAge {
        SUPER_YOUNG(0, 1),
        MEDIUM(1, 8),
        OLD(8, 25);
        
        public final int min, max;
        
        AnimalAge(int min, int max) {
            this.max = max;
            this.min = min;
        }
    }
    
    protected enum AnimalSize {
        SMALL(0.0, 3.0),
        MEDIUM(3.1, 5.0),
        BIG(5.1, 99.0);
        
        public final double min, max;
        
        AnimalSize(double min, double max) {
            this.min = min;
            this.max = max;
        }
    }
}
