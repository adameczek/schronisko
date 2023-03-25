package pl.inzynierka.schronisko.animals;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AnimalsRepository extends JpaRepository<Animal, Long>, JpaSpecificationExecutor<Animal> {
    int deleteById(long id);
}
