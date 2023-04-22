package pl.inzynierka.schronisko.animals.tags;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Getter
@Setter
public class AnimalTag {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Schema(description = "id of tag")
    private long id;
    @NotNull
    @Size(
            min = 1,
            max = 50
    )
    @Schema(
            description = "unique tag value",
            example = "rudy"
    )
    @Column(
            unique = true,
            nullable = false
    )
    private String value;
    
    public AnimalTag(String value) {
        this.value = value;
    }
}
