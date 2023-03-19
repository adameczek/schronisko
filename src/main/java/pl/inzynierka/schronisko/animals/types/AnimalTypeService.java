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

    public AnimalType saveAnimalType(AnimalType animalType) throws
            InsufficentUserRoleException, AnimalTypeServiceException {
        final User authenticatedUser =
                AuthenticationUtils.getAuthenticatedUser();
        if (authenticatedUser.hasNoRoles(Role.ADMIN, Role.MODERATOR)) {
            throw new InsufficentUserRoleException(
                    "To create animal types you need at least moderator role!");
        }

        try {
            return animalTypesRepository.save(animalType);
        } catch (DataIntegrityViolationException e) {
            throw new AnimalTypeServiceException(
                    "Podany typ zwierzęcia już istnieje.");
        }
    }

    public SimpleResponse deleteAnimalType(String value) throws
            InsufficentUserRoleException {
        final User authenticatedUser =
                AuthenticationUtils.getAuthenticatedUser();
        if (authenticatedUser.hasNoRoles(Role.ADMIN, Role.MODERATOR)) {
      throw new InsufficentUserRoleException(
          "To delete animal types you need at least moderator role!");
    }

    // todo add checking if any animals exist with given animal type

    final var result = animalTypesRepository.deleteByValue(value);

    return new SimpleResponse(result, null);
  }

  public Optional<AnimalType> findByValue(String value) {
    return animalTypesRepository.findFirstByValue(value);
  }
}
