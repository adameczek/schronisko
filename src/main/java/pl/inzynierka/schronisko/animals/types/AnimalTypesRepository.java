package pl.inzynierka.schronisko.animals.types;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnimalTypesRepository extends JpaRepository<AnimalType, Long> {
    Optional<AnimalType> findFirstByValue(String value);

    boolean deleteByValue(String value);
}
