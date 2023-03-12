package pl.inzynierka.schronisko.animals.tags;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AnimalTagsRepository extends MongoRepository<AnimalTag, String> {
  Optional<AnimalTag> findFirstByValue(String value);

  boolean deleteByValue(String value);
}
