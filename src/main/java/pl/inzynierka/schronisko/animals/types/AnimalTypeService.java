package pl.inzynierka.schronisko.animals.types;

import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.inzynierka.schronisko.animals.InsufficentUserRoleException;
import pl.inzynierka.schronisko.authentication.AuthenticationUtils;
import pl.inzynierka.schronisko.common.SimpleResponse;
import pl.inzynierka.schronisko.user.Role;
import pl.inzynierka.schronisko.user.User;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AnimalTypeService {
    private final AnimalTypesRepository animalTypesRepository;
    
    public Page<AnimalType> getAllTypes(Pageable pageable) {
        return animalTypesRepository.findAll(pageable);
    }
    
    public AnimalType saveAnimalType(AnimalType animalType)
            throws InsufficentUserRoleException, AnimalTypeServiceException {
        final User authenticatedUser = AuthenticationUtils.getAuthenticatedUser();
        if (authenticatedUser.hasNoRoles(Role.ADMIN, Role.MODERATOR)) {
            throw new InsufficentUserRoleException("To create animal types you need at least moderator role!");
        }
        
        try {
            return animalTypesRepository.save(animalType);
        } catch (DataIntegrityViolationException e) {
            throw new AnimalTypeServiceException("Podany typ zwierzęcia już istnieje.");
        }
    }
    
    public SimpleResponse deleteAnimalType(String value) throws InsufficentUserRoleException {
        final User authenticatedUser = AuthenticationUtils.getAuthenticatedUser();
        if (authenticatedUser.hasNoRoles(Role.ADMIN, Role.MODERATOR)) {
            throw new InsufficentUserRoleException("To delete animal types you need at least moderator role!");
        }
        
        // todo add checking if any animals exist with given animal type
        
        final var result = animalTypesRepository.deleteByValue(value);
        
        return new SimpleResponse(result, null);
    }
    
    public SimpleResponse addRaceToType(String race, String animalType) throws AnimalTypeServiceException {
        AnimalType type = animalTypesRepository.findFirstByValue(animalType)
                                               .orElseThrow(() -> new AnimalTypeServiceException(
                                                       "Nie znaleziono zwierząt o podanej rasie"));
        
        boolean result = type.getRaces().add(race);
        
        if (result) {
            animalTypesRepository.save(type);
            
            return new SimpleResponse(true, null);
        } else {
            return new SimpleResponse(false, "Dana rasa już istnieje dla: " + animalType);
        }
    }
    
    public Optional<AnimalType> findByValue(String value) {
        return animalTypesRepository.findFirstByValue(value);
    }
    
    public SimpleResponse removeRaceFromType(String race, String type) throws AnimalTypeServiceException {
        AnimalType animalType = animalTypesRepository.findFirstByValue(type)
                                                     .orElseThrow(() -> new AnimalTypeServiceException(
                                                             "Nie znaleziono zwierząt o podanej rasie"));
        
        boolean result = animalType.getRaces().remove(race);
        
        if (result) {
            animalTypesRepository.save(animalType);
            
            return new SimpleResponse(true, String.format("Usunięto rasę: %s z %s", race, type));
        } else {
            return new SimpleResponse(false, "Nie odnaleziono danej rasy w tej kategorii zwierząt!");
        }
    }
}
