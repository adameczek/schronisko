package pl.inzynierka.schronisko.animals;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.inzynierka.schronisko.authentication.AuthenticationUtils;
import pl.inzynierka.schronisko.common.MappingException;
import pl.inzynierka.schronisko.common.SimpleResponse;
import pl.inzynierka.schronisko.user.Role;
import pl.inzynierka.schronisko.user.User;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnimalService {
    private final AnimalsRepository animalsRepository;
    private final AnimalMapper animalMapper;

    public Page<Animal> getAnimals(Pageable pageable) {
        //todo limit sent data

        return animalsRepository.findAll(pageable);
    }

    public Optional<Animal> getAnimalById(long id) {
        return animalsRepository.findById(id);
    }

    public Animal createAnimal(AnimalRequest animal) throws
            InsufficentUserRoleException, AnimalServiceException {
        final User authenticatedUser =
                AuthenticationUtils.getAuthenticatedUser();

        if (authenticatedUser.getRoles()
                .stream()
                .noneMatch(role -> Role.ADMIN.equals(role) || Role.MODERATOR.equals(
                        role))) {

            throw new InsufficentUserRoleException(
                    "User is not authorized to add animals!");
        }

        try {
            var animalToSave = animalMapper.mapToAnimal(animal);
            animalToSave.setCreatedBy(authenticatedUser);
            animalToSave.setCreated(LocalDate.now());

            return animalsRepository.save(animalToSave);
        } catch (MappingException e) {
            throw new AnimalServiceException(e.getMessage());
        }
    }

    public Animal updateAnimal(Long id, String newAnimalDataRequest) throws
            InsufficentUserRoleException, AnimalServiceException {
        final User authenticatedUser =
                AuthenticationUtils.getAuthenticatedUser();

        if (authenticatedUser.hasNoRoles(Role.ADMIN, Role.MODERATOR)) {
            throw new InsufficentUserRoleException(
                    "Cannot update animal if authenticated user is not at least moderator.");
        }

        Animal existingAnimalData = animalsRepository.findById(id)
                .orElseThrow(() -> new AnimalServiceException(
                        "Nie znaleziono zwierzecia do zaktualizowania z danym id"));

        try {
            Animal mappedNewAnimalData = animalMapper.updateAnimal(
                    newAnimalDataRequest,
                    existingAnimalData);

            return animalsRepository.save(mappedNewAnimalData);
        } catch (BeansException e) {
            e.printStackTrace();
            throw new AnimalServiceException(
                    "Wystąpił błąd podczas aktualizowania zwierzaka!");
        } catch (MappingException e) {
            e.printStackTrace();
            throw new AnimalServiceException(e.getMessage());
        }
    }

    public SimpleResponse deleteAnimal(long id) throws
            InsufficentUserRoleException {
        final User authenticatedUser =
                AuthenticationUtils.getAuthenticatedUser();

        if (authenticatedUser.hasNoRoles(Role.ADMIN, Role.MODERATOR)) {
            throw new InsufficentUserRoleException(
                    "Cannot delete animal if user is not at least moderator.");
        }

        var result = animalsRepository.deleteById(id);

        return new SimpleResponse(result == 1, null);
    }

    public Page<Animal> searchForAnimals(AnimalSearchQuery searchQuery,
                                         Pageable pageable) {
        return null;
    }
}
