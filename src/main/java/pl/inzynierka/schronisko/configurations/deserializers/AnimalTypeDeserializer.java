package pl.inzynierka.schronisko.configurations.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.jackson.JsonComponent;
import pl.inzynierka.schronisko.animals.types.AnimalType;
import pl.inzynierka.schronisko.animals.types.AnimalTypeService;

@JsonComponent
@RequiredArgsConstructor
public class AnimalTypeDeserializer extends JsonDeserializer<AnimalType> {
  private final AnimalTypeService animalTypeService;

  @Override
  public AnimalType deserialize(
      JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
    TreeNode treeNode = jsonParser.getCodec().readTree(jsonParser);
    String value = jsonParser.readValueAs(String.class);

    return animalTypeService
        .findByValue(value)
        .orElseThrow(() -> new IOException("Nie znaleziono danego typu zwierzęcia w bazie."));
  }
}
