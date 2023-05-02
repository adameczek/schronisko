package pl.inzynierka.schronisko.animals;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import pl.inzynierka.schronisko.animals.tags.AnimalTag;
import pl.inzynierka.schronisko.animals.types.AnimalType;
import pl.inzynierka.schronisko.animals.types.Difficulty;
import pl.inzynierka.schronisko.configurations.validationscopes.RepositorySave;
import pl.inzynierka.schronisko.fileupload.ImageFileDTO;
import pl.inzynierka.schronisko.shelters.models.Shelter;
import pl.inzynierka.schronisko.user.User;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Entity
@Getter
@Setter
public class Animal {
    @ManyToOne
    @NotNull(groups = RepositorySave.class) User createdBy;
    @ManyToMany
    @Schema(description = "List of animal tags to help with searching")
    private List<AnimalTag> tags;
    @Nullable
    private Double weight;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    
    @NotNull(groups = RepositorySave.class)
    @ManyToOne
    private AnimalType type;
    @Nullable
    private String race;
    @Nullable
    private Sex sex;
    
    @NotNull
    @Size(
            min = 1,
            max = 50
    )
    @Schema(
            description = "Name of animal, cannot be null",
            example = "Peja"
    )
    @Column(nullable = false)
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
    
    @CreatedDate
    @Column(
            updatable = false,
            nullable = false
    )
    @Schema(description = "Date of animal creation")
    private LocalDateTime created;
    @LastModifiedDate
    private LocalDateTime updated;
    @ManyToOne(fetch = FetchType.LAZY)
    private Shelter shelter;
    @OneToMany(fetch = FetchType.EAGER)
    private List<ImageFileDTO> images;
    @Nullable
    private String color;
    @Nullable
    private Difficulty difficulty;
    @Nullable
    @Min(0)
    @Max(50)
    private Integer age;
    
}
