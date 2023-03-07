package pl.inzynierka.schronisko.animals.tags;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.inzynierka.schronisko.SimpleResponse;
import pl.inzynierka.schronisko.animals.InsufficentUserRoleException;
import pl.inzynierka.schronisko.authentication.AuthenticationUtils;
import pl.inzynierka.schronisko.user.Role;
import pl.inzynierka.schronisko.user.User;

@Service
@AllArgsConstructor
public class AnimalTagService {
    private final AnimalTagsRepository animalTagsRepository;

    public Page<AnimalTag> getAllTags(Pageable pageable) {
        return animalTagsRepository.findAll(pageable);
    }

    public AnimalTag saveTag(AnimalTag animalTag) throws
            InsufficentUserRoleException {
        final User authenticatedUser =
                AuthenticationUtils.getAuthenticatedUser();

        if (authenticatedUser.hasNoRoles(Role.ADMIN, Role.MODERATOR)) {
            throw new InsufficentUserRoleException(
                    "To create tags you need at least moderator role!");
        }

        return animalTagsRepository.save(animalTag);
    }

    public SimpleResponse deleteTag(String value) throws
            InsufficentUserRoleException {
        final User authenticatedUser =
                AuthenticationUtils.getAuthenticatedUser();
        if (authenticatedUser.hasNoRoles(Role.ADMIN, Role.MODERATOR)) {
            throw new InsufficentUserRoleException(
                    "To delete tags you need at least moderator role!");
        }

        final var result = animalTagsRepository.deleteByValue(value);


        //todo remove tags from existing animals

        return new SimpleResponse(result, null);
    }
}
