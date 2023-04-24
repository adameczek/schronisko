package pl.inzynierka.schronisko.configurations;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.inzynierka.schronisko.animals.Animal;
import pl.inzynierka.schronisko.animals.AnimalResponse;
import pl.inzynierka.schronisko.configurations.converters.TagListConverter;
import pl.inzynierka.schronisko.fileupload.ImageFileDTO;
import pl.inzynierka.schronisko.fileupload.ImageFileResponse;
import pl.inzynierka.schronisko.imagescaler.ResolutionEnum;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class AppConfiguration {
    @Bean
    public ModelMapper modelMapper() {
        var modelmapper = new ModelMapper();
        setAnimalToAnimalResponseMapping(modelmapper);
        return modelmapper;
    }
    
    private void setAnimalToAnimalResponseMapping(ModelMapper modelMapper) {
        TypeMap<Animal, AnimalResponse> typeMap = modelMapper.createTypeMap(Animal.class, AnimalResponse.class);
        
        typeMap.addMappings(mapper -> mapper.map(src -> src.getType().getValue(), AnimalResponse::setType));
        typeMap.addMappings(mapper -> mapper.using(new TagListConverter())
                                            .map(Animal::getTags, AnimalResponse::setTags));
        typeMap.addMappings(mapper -> mapper.when(mappingContext -> ((Animal) mappingContext.getParent()
                                                                                            .getSource()).getRace()
                                                                    != null)
                                            .map(Animal::getRace, AnimalResponse::setRace));
    }
    
    private void setImageToImageResponseMapping(ModelMapper modelMapper) {
        TypeMap<ImageFileDTO, ImageFileResponse> typeMap = modelMapper.createTypeMap(
                ImageFileDTO.class,
                ImageFileResponse.class);
        
        typeMap.addMapping(imageFileDTO -> {
            final Map<String, String> imageMap = new HashMap<>();
            for (ResolutionEnum resolutionEnum : ResolutionEnum.values()) {
                byte[] imageBytes;
                switch (resolutionEnum) {
                    case HIGH -> {
                        imageBytes = imageFileDTO.getBytes();
                    }
                    case MEDIUM -> {
                        imageBytes = imageFileDTO.getMediumBytes();
                    }
                    case MINI -> {
                        imageBytes = imageFileDTO.getMiniBytes();
                    }
                }
                imageMap.put()
            }
            
        })
    }
}
