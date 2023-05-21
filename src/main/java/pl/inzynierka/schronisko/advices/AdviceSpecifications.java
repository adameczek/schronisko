package pl.inzynierka.schronisko.advices;

import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import pl.inzynierka.schronisko.shelters.models.Shelter;

public class AdviceSpecifications {
    public static Specification<Advice> belongsToShelter(String shelterName) {
        return (root, query, criteriaBuilder) -> {
            Join<Shelter, Advice> shelter = root.join("shelter");
            
            return criteriaBuilder.equal(shelter.get("name"), shelterName);
        };
    }
}
