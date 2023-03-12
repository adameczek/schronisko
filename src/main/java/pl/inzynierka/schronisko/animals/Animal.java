package pl.inzynierka.schronisko.animals;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import pl.inzynierka.schronisko.animals.tags.AnimalTag;
import pl.inzynierka.schronisko.animals.types.AnimalType;
import pl.inzynierka.schronisko.configurations.deserializers.AnimalTagsDeserializer;
import pl.inzynierka.schronisko.configurations.deserializers.AnimalTypeDeserializer;
import pl.inzynierka.schronisko.configurations.validationscopes.RepositorySave;
import pl.inzynierka.schronisko.user.User;

@Document
@AllArgsConstructor
@Builder
@Data
public class Animal {
  @DBRef
  @Schema(description = "List of animal tags to help with searching")
  @JsonDeserialize(using = AnimalTagsDeserializer.class)
  List<AnimalTag> tags;

  @DBRef
  @NotNull(groups = RepositorySave.class)
  User createdBy;

  @Id private String id;

  @DBRef
  @NotNull(groups = RepositorySave.class)
  @JsonDeserialize(using = AnimalTypeDeserializer.class)
  private AnimalType type;

  @NotNull
  @Size(min = 1, max = 50)
  @Schema(description = "Name of animal, cannot be null", example = "Peja")
  private String name;

  @Size(min = 0, max = 1000)
  @Schema(description = "Description of animal", example = "mily, gryzie tylko dzieci")
  private String description;
}
