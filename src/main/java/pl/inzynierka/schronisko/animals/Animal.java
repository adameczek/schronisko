package pl.inzynierka.schronisko.animals;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import pl.inzynierka.schronisko.animals.tags.AnimalTag;
import pl.inzynierka.schronisko.animals.types.AnimalType;
import pl.inzynierka.schronisko.configurations.validationscopes.RepositorySave;
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
    @ManyToMany
    @Schema(description = "List of animal tags to help with searching")
    private List<AnimalTag> tags;

    @ManyToOne
    @NotNull(groups = RepositorySave.class) User createdBy;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull(groups = RepositorySave.class)
    @ManyToOne
    private AnimalType type;

    @NotNull
    @Size(min = 1, max = 50)
    @Schema(description = "Name of animal, cannot be null", example = "Peja")
    @Column(nullable = false)
    private String name;

    @Size(min = 0, max = 1000)
    @Schema(
            description = "Description of animal",
            example = "mily, gryzie tylko dzieci"
    )
    private String description;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    @Schema(description = "Date of animal creation")
    private LocalDateTime created;
    @LastModifiedDate
    private LocalDateTime updated;
}