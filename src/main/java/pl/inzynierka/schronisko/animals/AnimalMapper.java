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
import pl.inzynierka.schronisko.configurations.converters.ImageListToRequestConverter;
import pl.inzynierka.schronisko.fileupload.ImageFileDTO;

import java.io.IOException;
import java.util.ArrayList;
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
        typeMap.addMappings(ex -> ex.using(new ImageListToRequestConverter())
                                    .map(Animal::getImages, AnimalRequest::setImages));
        
        return mapper.map(animalToMap, AnimalRequest.class);
    }
    
    public Animal mapToAnimal(AnimalRequest animalRequest, Animal existingAnimal) {
        ModelMapper mapper = new ModelMapper();
        TypeMap<AnimalRequest, Animal> typeMap = mapper.createTypeMap(AnimalRequest.class, Animal.class);
        
        typeMap.addMappings(ex -> ex.using(stringAnimalTypeConverter())
                                    .map(AnimalRequest::getType, Animal::setType));
        typeMap.addMappings(ex -> ex.using(listToAnimalTagList()).map(AnimalRequest::getTags, Animal::setTags));
        typeMap.addMappings(ex -> ex.when(raceExists()).map(AnimalRequest::getRace, Animal::setRace));
        typeMap.addMappings(ex -> ex.using(listIdsToFileList()).map(AnimalRequest::getImages, Animal::setImages));
        
        mapper.map(animalRequest, existingAnimal);
        
        return existingAnimal;
    }
    
    private Converter<List<Long>, List<ImageFileDTO>> listIdsToFileList() {
        return c -> {
            if (c.getSource() == null || c.getSource().size() == 0)
                return Collections.emptyList();
            return new ArrayList<>(c.getSource()
                                    .stream()
                                    .map(id -> new ImageFileDTO(id, null, null, null, null, null))
                                    .toList());
        };
    }
    
    
    public Animal mapToAnimal(AnimalRequest animalRequest) throws MappingException {
        ModelMapper mapper = new ModelMapper();
        TypeMap<AnimalRequest, Animal> typeMap = mapper.createTypeMap(AnimalRequest.class, Animal.class);
        typeMap.addMappings(ex -> ex.using(stringAnimalTypeConverter())
                                    .map(AnimalRequest::getType, Animal::setType));
        typeMap.addMappings(ex -> ex.when(raceExists()).map(AnimalRequest::getRace, Animal::setRace));
        typeMap.addMappings(ex -> ex.using(listToAnimalTagList()).map(AnimalRequest::getTags, Animal::setTags));
        typeMap.addMappings(ex -> ex.using(listIdsToFileList()).map(AnimalRequest::getImages, Animal::setImages));
        
        return mapper.map(animalRequest, Animal.class);
    }
    
    Converter<AnimalType, String> animalTypeStringConverter() {
        return c -> c.getSource().getValue();
    }
    
    Converter<List<AnimalTag>, List<String>> animalTagListStringListConverter() {
        return c -> Optional.ofNullable(c.getSource())
                            .map(animalTags -> animalTags.stream()
                                                         .map(AnimalTag::getValue)
                                                         .collect(Collectors.toList()))
                            .orElse(Collections.emptyList());
    }
    
    private Converter<String, AnimalType> stringAnimalTypeConverter() {
        return c -> animalTypeService.findByValue(c.getSource()).orElseThrow(() -> new MappingException(
                "Nie znaleziono podanego typu zwierzęcia: " + c.getSource()));
    }
    
    private Converter<List<String>, List<AnimalTag>> listToAnimalTagList() {
        return c -> {
            if (c.getSource() == null || c.getSource().size() == 0)
                return Collections.emptyList();
            
            return c.getSource()
                    .stream()
                    .map(s -> animalTagService.findByValue(s)
                                              .orElseThrow(() -> new MappingException(
                                                      "Nie znaleziono podanego tagu: " + s)))
                    .collect(Collectors.toList());
        };
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
