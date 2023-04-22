package pl.inzynierka.schronisko.animals;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import pl.inzynierka.schronisko.common.MappingException;
import pl.inzynierka.schronisko.common.SimpleResponse;
import pl.inzynierka.schronisko.user.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnimalService {
    private final AnimalsRepository animalsRepository;
    private final AnimalMapper animalMapper;
    private final EntityManager entityManager;
    
    public Page<Animal> getAnimals(Pageable pageable) {
        //todo limit sent data
        
        return animalsRepository.findAll(pageable);
    }
    
    public Optional<Animal> getAnimalById(long id) {
        return animalsRepository.findById(id);
    }
    
    public Animal createAnimal(AnimalRequest animal, User requestCreator) throws AnimalServiceException {
        
        try {
            var animalToSave = animalMapper.mapToAnimal(animal);
            animalToSave.setCreatedBy(requestCreator);
            animalToSave.setCreated(LocalDateTime.now());
            
            return animalsRepository.save(animalToSave);
        } catch (MappingException e) {
            throw new AnimalServiceException(e.getMessage());
        }
    }
    
    public Animal updateAnimal(Long id, String newAnimalDataRequest) throws AnimalServiceException {
        Animal existingAnimalData = animalsRepository.findById(id).orElseThrow(() -> new AnimalServiceException(
                "Nie znaleziono zwierzecia do zaktualizowania z danym id"));
        
        try {
            Animal mappedNewAnimalData = animalMapper.updateAnimal(newAnimalDataRequest, existingAnimalData);
            
            return animalsRepository.save(mappedNewAnimalData);
        } catch (BeansException e) {
            e.printStackTrace();
            throw new AnimalServiceException("Wystąpił błąd podczas aktualizowania zwierzaka!");
        } catch (MappingException e) {
            e.printStackTrace();
            throw new AnimalServiceException(e.getMessage());
        }
    }
    
    public SimpleResponse deleteAnimal(long id) {
        var result = animalsRepository.deleteById(id);
        
        return new SimpleResponse(result == 1, null);
    }
    
    public Page<Animal> searchForAnimals(AnimalSearchQuery searchQuery, Pageable pageable)
            throws AnimalServiceException {
        //todo add validator
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        Specification<Animal> specification = Specification.where(null);
        
        if (Objects.nonNull(searchQuery.getName()))
            specification = specification.and(AnimalSpecifications.hasNameLike(searchQuery.getName()));
        
        if (Objects.nonNull(searchQuery.getCreatedBy()))
            specification = specification.and(AnimalSpecifications.createdBy(searchQuery.getCreatedBy()));
        
        if (Objects.nonNull(searchQuery.getTypes()))
            specification = specification.and(AnimalSpecifications.hasTypeIn(searchQuery.getTypes()));
        
        if (Objects.nonNull(searchQuery.getTags()))
            specification = specification.and(AnimalSpecifications.hasTags(searchQuery.getTags()));
        
        PageRequest pageRequest;
        
        if (searchQuery.getSortBy() != null) {
            pageRequest = PageRequest.of(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    getSortByFromString(searchQuery.getSortBy()));
        } else {
            pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        }
        
        //todo add pagination and others
        return animalsRepository.findAll(specification, pageRequest);
    }
    
    Sort getSortByFromString(String sortBy) throws AnimalServiceException {
        if (sortBy.length() < 2)
            throw new AnimalServiceException("Niepoprawna wartość w sortBy!");
        
        Sort.Direction direction;
        if (sortBy.charAt(0) == '+') {
            direction = Sort.Direction.ASC;
        } else if (sortBy.charAt(0) == '-') {
            direction = Sort.Direction.DESC;
        } else {
            throw new AnimalServiceException("Niepoprawny pierwszy znak w polu sortBy!");
        }
        
        String field = sortBy.substring(1);
        
        var allowedFields = List.of("type", "name", "createdBy", "created");
        
        if (allowedFields.contains(field)) {
            return Sort.by(direction, field);
        } else {
            throw new AnimalServiceException("Niepoprawna nazwa pola do sortowania!");
        }
    }
}
