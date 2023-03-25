package pl.inzynierka.schronisko.animals.tags;

import lombok.AllArgsConstructor;
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
public class AnimalTagService {
  private final AnimalTagsRepository animalTagsRepository;

  public Page<AnimalTag> getAllTags(Pageable pageable) {
    return animalTagsRepository.findAll(pageable);
  }

  public AnimalTag saveTag(AnimalTag animalTag)
      throws InsufficentUserRoleException, AnimalTagServiceException {
    final User authenticatedUser = AuthenticationUtils.getAuthenticatedUser();

    if (authenticatedUser.hasNoRoles(Role.ADMIN, Role.MODERATOR)) {
      throw new InsufficentUserRoleException(
              "Potrzebujesz uprawnień conajmniej moderatora żeby utworzyć tag!");
    }

    try {
      return animalTagsRepository.save(animalTag);
    } catch (Exception e) {
      e.printStackTrace();
      throw new AnimalTagServiceException(
              "Wystąpił błąd przy zapisie nowego tag'a");
    }
  }

    public SimpleResponse deleteTag(String value) throws
            InsufficentUserRoleException {
        final User authenticatedUser =
                AuthenticationUtils.getAuthenticatedUser();
        if (authenticatedUser.hasNoRoles(Role.ADMIN, Role.MODERATOR)) {
      throw new InsufficentUserRoleException("To delete tags you need at least moderator role!");
    }

    final var result = animalTagsRepository.deleteByValue(value);

    // todo remove tags from existing animals

    return new SimpleResponse(result, null);
  }

  public Optional<AnimalTag> findByValue(String value) {
    return animalTagsRepository.findFirstByValue(value);
  }
}
