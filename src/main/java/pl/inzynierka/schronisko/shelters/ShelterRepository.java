package pl.inzynierka.schronisko.shelters;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.inzynierka.schronisko.shelters.models.Shelter;

import java.util.Optional;

public interface ShelterRepository extends JpaRepository<Shelter, Long> {
    Long deleteByName(String name);

    Optional<Shelter> findFirstByName(String name);
}
