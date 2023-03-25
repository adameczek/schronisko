package pl.inzynierka.schronisko.animals.tags;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnimalTagsRepository extends JpaRepository<AnimalTag, Long> {
  Optional<AnimalTag> findFirstByValue(String value);

  boolean deleteByValue(String value);
}
