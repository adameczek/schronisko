package pl.inzynierka.schronisko.news;

import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import pl.inzynierka.schronisko.shelters.models.Shelter;

public class NewsSpecifications {
    public static Specification<News> belongsToShelter(String shelterName) {
        return (root, query, criteriaBuilder) -> {
            Join<Shelter, News> shelter = root.join("shelter");
            
            return criteriaBuilder.equal(shelter.get("name"), shelterName);
        };
    }
}
