package pl.inzynierka.schronisko.initialtest;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HelloRepository extends ReactiveMongoRepository<Hello, String> {

}
