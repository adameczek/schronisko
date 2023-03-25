package pl.inzynierka.schronisko.configurations;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.inzynierka.schronisko.animals.Animal;
import pl.inzynierka.schronisko.animals.AnimalResponse;
import pl.inzynierka.schronisko.configurations.converters.TagListConverter;

@Configuration
public class AppConfiguration {
    @Bean
    public ModelMapper modelMapper() {
        var modelmapper = new ModelMapper();
        setAnimalToAnimalResponseMapping(modelmapper);

        return modelmapper;
    }

    private void setAnimalToAnimalResponseMapping(ModelMapper modelMapper) {
        TypeMap<Animal, AnimalResponse> typeMap = modelMapper.createTypeMap(
                Animal.class,
                AnimalResponse.class);

        typeMap.addMappings(
                mapper -> mapper.map(src -> src.getType().getValue(),
                                     AnimalResponse::setType)
        );
        typeMap.addMappings(
                mapper -> mapper.using(new TagListConverter())
                        .map(Animal::getTags, AnimalResponse::setTags)
        );
    }
}
