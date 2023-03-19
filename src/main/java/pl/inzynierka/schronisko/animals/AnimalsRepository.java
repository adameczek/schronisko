package pl.inzynierka.schronisko.animals;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AnimalsRepository extends JpaRepository<Animal, Long> {
    int deleteById(long id);
}
