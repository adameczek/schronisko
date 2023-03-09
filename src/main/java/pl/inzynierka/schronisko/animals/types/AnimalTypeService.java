package pl.inzynierka.schronisko.animals.types;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.inzynierka.schronisko.animals.InsufficentUserRoleException;
import pl.inzynierka.schronisko.authentication.AuthenticationUtils;
import pl.inzynierka.schronisko.common.SimpleResponse;
import pl.inzynierka.schronisko.user.Role;
import pl.inzynierka.schronisko.user.User;

@Service
@AllArgsConstructor
public class AnimalTypeService {
    private final AnimalTypesRepository animalTypesRepository;

    public Page<AnimalType> getAllTypes(Pageable pageable) {
        return animalTypesRepository.findAll(pageable);
    }

    public AnimalType saveAnimalType(AnimalType animalType) throws
            InsufficentUserRoleException {
        final User authenticatedUser =
                AuthenticationUtils.getAuthenticatedUser();
        if (authenticatedUser.hasNoRoles(Role.ADMIN, Role.MODERATOR)) {
            throw new InsufficentUserRoleException(
                    "To create animal types you need at least moderator role!");
        }

        return animalTypesRepository.save(animalType);
    }

    public SimpleResponse deleteAnimalType(String value) throws
            InsufficentUserRoleException {
        final User authenticatedUser =
                AuthenticationUtils.getAuthenticatedUser();
        if (authenticatedUser.hasNoRoles(Role.ADMIN, Role.MODERATOR)) {
            throw new InsufficentUserRoleException(
                    "To delete animal types you need at least moderator role!");
        }

        //todo add checking if any animals exist with given animal type

        final var result = animalTypesRepository.deleteByValue(value);

        return new SimpleResponse(result, null);
    }
}
