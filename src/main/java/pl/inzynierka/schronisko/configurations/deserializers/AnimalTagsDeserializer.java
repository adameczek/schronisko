package pl.inzynierka.schronisko.configurations.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.jackson.JsonComponent;
import pl.inzynierka.schronisko.animals.tags.AnimalTag;
import pl.inzynierka.schronisko.animals.tags.AnimalTagService;

@JsonComponent
@RequiredArgsConstructor
public class AnimalTagsDeserializer extends JsonDeserializer<List<AnimalTag>> {
  private final AnimalTagService animalTagService;

  @Override
  public List<AnimalTag> deserialize(
      JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
    TreeNode treeNode = jsonParser.getCodec().readTree(jsonParser);

    if (!treeNode.isArray()) throw new IOException("\"tags\" nie jest listą!");

    ArrayNode list = (ArrayNode) treeNode;
    var iterator = list.iterator();

    List<AnimalTag> tags = new ArrayList<>();
    while (iterator.hasNext()) {
      var tag = iterator.next().asText();

      AnimalTag foundTag =
          animalTagService
              .findByValue(tag)
              .orElseThrow(
                  () -> new IOException("Nie znaleziono danego tagu: " + tag + " w bazie!"));

      tags.add(foundTag);
    }

    return tags;
  }
}
