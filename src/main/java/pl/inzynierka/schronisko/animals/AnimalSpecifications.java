package pl.inzynierka.schronisko.animals;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import pl.inzynierka.schronisko.animals.tags.AnimalTag;
import pl.inzynierka.schronisko.animals.types.AnimalType;
import pl.inzynierka.schronisko.user.User;

import java.util.List;
import java.util.NoSuchElementException;

public class AnimalSpecifications {
    public static Specification<Animal> hasNameLike(String name) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), "%" + name + "%");
    }
    
    public static Specification<Animal> hasTags(List<String> tags) {
        return ((root, query, criteriaBuilder) -> {
            Join<AnimalTag, Animal> animalTags = root.join("tags");
            
            CriteriaBuilder.In<Object> value = criteriaBuilder.in(animalTags.get("value"));
            for (String tag : tags) {
                value.value(tag);
            }
            
            return value;
        });
    }
    
    public static Specification<Animal> hasTypeIn(List<String> types) {
        return ((root, query, criteriaBuilder) -> {
            Join<AnimalType, Animal> animalType = root.join("type");
            
            CriteriaBuilder.In<Object> value = criteriaBuilder.in(animalType.get("value"));
            
            for (var type : types) {
                value.value(type);
            }
            
            return value;
        });
    }
    
    public static Specification<Animal> hasTypeOtherThan(List<String> types) {
        return ((root, query, criteriaBuilder) -> {
            Join<AnimalType, Animal> animalType = root.join("type");
            
            CriteriaBuilder.In<Object> value = criteriaBuilder.in(animalType.get("value"));
            
            for (var type : types) {
                value.value(type);
            }
            
            return criteriaBuilder.not(value);
        });
    }
    
    public static Specification<Animal> createdBy(String email) {
        return (root, query, criteriaBuilder) -> {
            Join<User, Animal> createdBy = root.join("users");
            return criteriaBuilder.equal(createdBy.get("email"), email);
        };
    }
    
    public static Specification<Animal> hasSex(List<Sex> sex) {
        return (root, query, criteriaBuilder) -> {
            CriteriaBuilder.In<Object> criteriaIn = criteriaBuilder.in(root.get("sex"));
            sex.forEach(criteriaIn::value);
            
            return criteriaIn;
        };
    }
    
    public static Specification<Animal> hasAgeBetween(List<AnimalSearchQuery.AnimalAge> age) {
        return (root, query, criteriaBuilder) -> {
            double min = age.stream()
                            .mapToDouble(value -> value.min)
                            .min()
                            .orElseThrow(NoSuchElementException::new);
            double max = age.stream()
                            .mapToDouble(value -> value.max)
                            .max()
                            .orElseThrow(NoSuchElementException::new);
            return criteriaBuilder.between(root.get("age"), min, max);
        };
    }
    
    public static Specification<Animal> hasWeightBetween(List<AnimalSearchQuery.AnimalSize> size) {
        return (root, query, criteriaBuilder) -> {
            double min = size.stream()
                             .mapToDouble(value -> value.min)
                             .min()
                             .orElseThrow(NoSuchElementException::new);
            double max = size.stream()
                             .mapToDouble(value -> value.max)
                             .max()
                             .orElseThrow(NoSuchElementException::new);
            return criteriaBuilder.between(root.get("weight"), min, max);
        };
    }
}
