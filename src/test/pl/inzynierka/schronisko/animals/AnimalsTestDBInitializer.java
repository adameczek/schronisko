package pl.inzynierka.schronisko.animals;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.inzynierka.schronisko.animals.tags.AnimalTag;
import pl.inzynierka.schronisko.animals.tags.AnimalTagsRepository;
import pl.inzynierka.schronisko.animals.types.AnimalType;
import pl.inzynierka.schronisko.animals.types.AnimalTypesRepository;

@Component
@RequiredArgsConstructor
public class AnimalsTestDBInitializer {
    private final AnimalsRepository animalsRepository;
    private final AnimalTagsRepository animalTagsRepository;
    private final AnimalTypesRepository animalTypesRepository;
    
    public void initDB() {
        initAnimalTypes();
        initAnimalTags();
    }
    
    public void initAnimalTypes() {
        animalTypesRepository.save(new AnimalType("kot"));
        animalTypesRepository.save(new AnimalType("pies"));
    }
    
    public void initAnimalTags() {
        animalTagsRepository.save(new AnimalTag("rudy"));
        animalTagsRepository.save(new AnimalTag("ruchliwy"));
        animalTagsRepository.save(new AnimalTag("lubi dzieci"));
    }
}
