package pl.inzynierka.schronisko.animals.types;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class AnimalType {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Schema(description = "id of animal type")
    private Long id;
    @NotNull
    @Size(
            min = 1,
            max = 100
    )
    @Column(
            nullable = false,
            unique = true
    )
    @Schema(
            description = "Type of animal",
            example = "pies"
    )
    private String value;
    @Schema(description = "Available races to this animal type")
    @ElementCollection
    private Set<String> races = new HashSet<>();
    
    public AnimalType(String value) {
        this.value = value;
    }
}
