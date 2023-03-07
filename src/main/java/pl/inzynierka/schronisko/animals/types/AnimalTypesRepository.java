package pl.inzynierka.schronisko.animals.types;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface AnimalTypesRepository extends MongoRepository<AnimalType, String> {
    boolean deleteByValue(String value);
}
