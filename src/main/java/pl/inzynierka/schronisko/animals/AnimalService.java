package pl.inzynierka.schronisko.animals;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.result.DeleteResult;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import pl.inzynierka.schronisko.authentication.AuthenticationUtils;
import pl.inzynierka.schronisko.common.SimpleResponse;
import pl.inzynierka.schronisko.user.Role;
import pl.inzynierka.schronisko.user.User;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnimalService {
    private final AnimalsRepository animalsRepository;
    private final MongoTemplate mongoTemplate;
    private final FindAndModifyOptions findAndModifyOptions;
    private final ObjectMapper objectMapper;

    public Page<Animal> getAnimals(Pageable pageable) {
        //todo limit sent data

        return animalsRepository.findAll(pageable);
    }

    public Optional<Animal> getAnimalById(String id) {
        return animalsRepository.findById(id);
    }

    public Animal createAnimal(Animal animal) throws
            InsufficentUserRoleException {
        final User authenticatedUser =
                AuthenticationUtils.getAuthenticatedUser();

        if (authenticatedUser.getRoles()
                .stream()
                .noneMatch(role -> Role.ADMIN.equals(role) || Role.MODERATOR.equals(
                        role))) {

            throw new InsufficentUserRoleException(
                    "User is not authorized to add animals!");
        }

        animal.setCreatedBy(authenticatedUser);

        return animalsRepository.save(animal);
    }

    public Animal updateAnimal(String id, Animal newAnimalData) throws
            InsufficentUserRoleException, AnimalServiceException {
        final User authenticatedUser =
                AuthenticationUtils.getAuthenticatedUser();

        if (authenticatedUser.hasNoRoles(Role.ADMIN,
                                         Role.MODERATOR)) {
            throw new InsufficentUserRoleException(
                    "Cannot update animal if authenticated user is not at least moderator.");
        }

        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));

        var update = new Update();

        Map<String, Object> animalFieldMap = objectMapper.convertValue(
                newAnimalData,
                Map.class);
        animalFieldMap.forEach(update::set);

        Animal updatedAnimal = mongoTemplate.findAndModify(query,
                                                           update,
                                                           findAndModifyOptions,
                                                           Animal.class);

        if (Objects.isNull(updatedAnimal)) {
            throw new AnimalServiceException(
                    "animal for updating has not been found!");
        }

        return updatedAnimal;
    }

    public SimpleResponse deleteAnimal(String id) throws
            InsufficentUserRoleException {
        final User authenticatedUser =
                AuthenticationUtils.getAuthenticatedUser();

        if (authenticatedUser.hasNoRoles(Role.ADMIN, Role.MODERATOR)) {
            throw new InsufficentUserRoleException(
                    "Cannot delete animal if user is not at least moderator.");
        }

        final var query = new Query();
        query.addCriteria(Criteria.where("id").is(id));

        DeleteResult remove = mongoTemplate.remove(query, Animal.class);

        if (remove.getDeletedCount() == 0) {
            return new SimpleResponse(false, null);
        }

        return new SimpleResponse(true, null);
    }
}
