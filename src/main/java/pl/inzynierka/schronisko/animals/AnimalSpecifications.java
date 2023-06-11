package pl.inzynierka.schronisko.animals;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import pl.inzynierka.schronisko.animals.tags.AnimalTag;
import pl.inzynierka.schronisko.animals.types.AnimalType;
import pl.inzynierka.schronisko.user.User;

import java.util.List;

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
    
    public static Specification<Animal> hasSex(Sex sex) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("sex"), sex);
    }
    
    public static Specification<Animal> hasAgeBetween(AnimalSearchQuery.AnimalAge age) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.between(root.get("age"), age.min, age.max);
    }
    
    public static Specification<Animal> hasWeightBetween(AnimalSearchQuery.AnimalSize size) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.between(root.get("weight"), size.min, size.max);
    }
}
