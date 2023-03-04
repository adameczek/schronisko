package pl.inzynierka.schronisko.animals.tags;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface AnimalTagsRepository extends MongoRepository<AnimalTag, String> {
}
