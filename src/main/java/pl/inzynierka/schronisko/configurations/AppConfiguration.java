package pl.inzynierka.schronisko.configurations;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import pl.inzynierka.schronisko.animals.Animal;
import pl.inzynierka.schronisko.animals.AnimalResponse;
import pl.inzynierka.schronisko.configurations.converters.TagListConverter;

@Configuration
public class AppConfiguration {
    @Bean
    public ModelMapper modelMapper(Environment environment) {
        var modelmapper = new ModelMapper();
        setAnimalToAnimalResponseMapping(modelmapper, environment);
        return modelmapper;
    }
    
    private void setAnimalToAnimalResponseMapping(ModelMapper modelMapper, Environment environment) {
        TypeMap<Animal, AnimalResponse> typeMap = modelMapper.createTypeMap(Animal.class, AnimalResponse.class);
        final String baseUrl = environment.getProperty("settings.url");
        
        typeMap.addMappings(mapper -> mapper.map(src -> src.getType().getValue(), AnimalResponse::setType));
        typeMap.addMappings(mapper -> mapper.using(new TagListConverter())
                                            .map(Animal::getTags, AnimalResponse::setTags));
        typeMap.addMappings(mapper -> mapper.when(mappingContext -> ((Animal) mappingContext.getParent()
                                                                                            .getSource()).getRace()
                                                                    != null)
                                            .map(Animal::getRace, AnimalResponse::setRace));
        typeMap.addMappings(mapper -> mapper.using(new ImageListConverter(baseUrl)).map(Animal::getImages, AnimalResponse::setImages));
    }
}