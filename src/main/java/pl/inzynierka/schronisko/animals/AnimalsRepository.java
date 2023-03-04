package pl.inzynierka.schronisko.animals;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface AnimalsRepository extends MongoRepository<Animal, String> {
}
