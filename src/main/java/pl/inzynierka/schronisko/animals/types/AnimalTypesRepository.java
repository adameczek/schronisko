package pl.inzynierka.schronisko.animals.types;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AnimalTypesRepository extends MongoRepository<AnimalType, String> {
  Optional<AnimalType> findFirstByValue(String value);

  boolean deleteByValue(String value);
}
