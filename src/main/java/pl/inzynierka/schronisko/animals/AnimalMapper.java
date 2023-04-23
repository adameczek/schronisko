package pl.inzynierka.schronisko.animals;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Condition;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Service;
import pl.inzynierka.schronisko.animals.tags.AnimalTag;
import pl.inzynierka.schronisko.animals.tags.AnimalTagService;
import pl.inzynierka.schronisko.animals.types.AnimalType;
import pl.inzynierka.schronisko.animals.types.AnimalTypeService;
import pl.inzynierka.schronisko.common.MappingException;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnimalMapper {
    private final AnimalTypeService animalTypeService;
    private final AnimalTagService animalTagService;
    
    public Animal updateAnimal(String request, Animal animal) throws MappingException {
        ModelMapper mapper = new ModelMapper();
        ObjectMapper ob = new ObjectMapper();
        mapper.getConfiguration().setSkipNullEnabled(true);
        
        try {
            AnimalRequest animalFromRequest = ob.readerForUpdating(mapAnimalToRequest(animal)).withoutAttribute(
                    "createdBy").withoutAttribute("id").readValue(request, AnimalRequest.class);
            
            return mapToAnimal(animalFromRequest, animal);
            
        } catch (IOException e) {
            throw new MappingException(e);
        }
    }
    
    public AnimalRequest mapAnimalToRequest(Animal animalToMap) {
        ModelMapper mapper = new ModelMapper();
        TypeMap<Animal, AnimalRequest> typeMap = mapper.createTypeMap(Animal.class, AnimalRequest.class);
        
        typeMap.addMappings(ex -> ex.using(animalTypeStringConverter())
                                    .map(Animal::getType, AnimalRequest::setType));
        typeMap.addMappings(ex -> ex.using(animalTagListStringListConverter())
                                    .map(Animal::getTags, AnimalRequest::setTags));
        
        return mapper.map(animalToMap, AnimalRequest.class);
    }
    
    public Animal mapToAnimal(AnimalRequest animalRequest, Animal existingAnimal) {
        ModelMapper mapper = new ModelMapper();
        TypeMap<AnimalRequest, Animal> typeMap = mapper.createTypeMap(AnimalRequest.class, Animal.class);
        
        typeMap.addMappings(ex -> ex.using(stringAnimalTypeConverter())
                                    .map(AnimalRequest::getType, Animal::setType));
        typeMap.addMappings(ex -> ex.using(listToAnimalTagList()).map(AnimalRequest::getTags, Animal::setTags));
        typeMap.addMappings(ex -> ex.when(raceExists()).map(AnimalRequest::getRace, Animal::setRace));
        
        mapper.map(animalRequest, existingAnimal);
        
        return existingAnimal;
    }
    
    public Animal mapToAnimal(AnimalRequest animalRequest) throws MappingException {
        ModelMapper mapper = new ModelMapper();
        TypeMap<AnimalRequest, Animal> typeMap = mapper.createTypeMap(AnimalRequest.class, Animal.class);
        typeMap.addMappings(ex -> ex.using(stringAnimalTypeConverter())
                                    .map(AnimalRequest::getType, Animal::setType));
        typeMap.addMappings(ex -> ex.when(raceExists()).map(AnimalRequest::getRace, Animal::setRace));
        typeMap.addMappings(ex -> ex.using(listToAnimalTagList()).map(AnimalRequest::getTags, Animal::setTags));
        
        
        return mapper.map(animalRequest, Animal.class);
    }
    
    Converter<AnimalType, String> animalTypeStringConverter() {
        return c -> c.getSource().getValue();
    }
    
    Converter<List<AnimalTag>, List<String>> animalTagListStringListConverter() {
        return c -> c.getSource().stream().map(AnimalTag::getValue).collect(Collectors.toList());
    }
    
    private Converter<String, AnimalType> stringAnimalTypeConverter() {
        return c -> animalTypeService.findByValue(c.getSource()).orElseThrow(() -> new MappingException(
                "Nie znaleziono podanego typu zwierzęcia: " + c.getSource()));
    }
    
    private Converter<List<String>, List<AnimalTag>> listToAnimalTagList() {
        return c -> Optional.ofNullable(c.getSource()).map(strings -> strings.stream()
                                                                             .map(s -> animalTagService.findByValue(
                                                                                                               s)
                                                                                                       .orElseThrow(
                                                                                                               () -> new MappingException(
                                                                                                                       "Nie znaleziono podanego tagu: "
                                                                                                                       + s)))
                                                                             .collect(Collectors.toList())).orElse(
                Collections.emptyList());
        
    }
    
    private Condition<String, String> raceExists() {
        return c -> {
            if (c.getSource() == null)
                return false;
            
            var destination = (Animal) c.getParent().getDestination();
            var source = (AnimalRequest) c.getParent().getSource();
            
            AnimalType animalType = null;
            if (source.getType() != null) {
                animalType = animalTypeService.findByValue(((AnimalRequest) c.getParent().getSource()).getType())
                                              .orElseThrow(() -> new MappingException(
                                                      "Nie można ustalić rasy bez podanego typu " + "zwierzęcia"));
            } else if (destination.getType() != null) {
                animalType = destination.getType();
            }
            
            var result = animalType.getRaces().contains(c.getSource());
            
            if (!result)
                throw new MappingException("Nie znaleziono rasy w podanym typie zwierzęcia!");
            
            return true;
        };
    }
    
    
}
