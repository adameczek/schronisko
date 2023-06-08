package pl.inzynierka.schronisko.configurations;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import pl.inzynierka.schronisko.advices.Advice;
import pl.inzynierka.schronisko.advices.AdviceResponse;
import pl.inzynierka.schronisko.animals.Animal;
import pl.inzynierka.schronisko.animals.AnimalResponse;
import pl.inzynierka.schronisko.configurations.converters.ImageListConverter;
import pl.inzynierka.schronisko.configurations.converters.TagListConverter;
import pl.inzynierka.schronisko.news.News;
import pl.inzynierka.schronisko.news.NewsResponse;

@Configuration
public class AppConfiguration {
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        return jacksonObjectMapperBuilder -> jacksonObjectMapperBuilder.build().registerModule(new JavaTimeModule());
    }
    @Bean
    public ModelMapper modelMapper(Environment environment) {
        var modelmapper = new ModelMapper();
        setAnimalToAnimalResponseMapping(modelmapper, environment);
        setNewsToNewsResponseMapping(modelmapper);
        setAdviceToAdviceResponseMapping(modelmapper);
        return modelmapper;
    }
    
    private void setNewsToNewsResponseMapping(ModelMapper modelmapper) {
        TypeMap<News, NewsResponse> typeMap = modelmapper.createTypeMap(News.class, NewsResponse.class);
        
        typeMap.addMappings(mapper -> mapper.map(news -> news.getShelter().getName(), NewsResponse::setShelter));
        typeMap.addMappings(mapper -> mapper.map(news -> news.getAddedBy().getEmail(), NewsResponse::setAddedBy));
    }
    
    private void setAdviceToAdviceResponseMapping(ModelMapper modelmapper) {
        TypeMap<Advice, AdviceResponse> typeMap = modelmapper.createTypeMap(Advice.class, AdviceResponse.class);
        
        typeMap.addMappings(mapper -> mapper.map(news -> news.getShelter().getName(), AdviceResponse::setShelter));
        typeMap.addMappings(mapper -> mapper.map(news -> news.getAddedBy().getEmail(), AdviceResponse::setAddedBy));
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