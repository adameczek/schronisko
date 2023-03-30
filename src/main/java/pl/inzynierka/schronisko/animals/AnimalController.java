package pl.inzynierka.schronisko.animals;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import pl.inzynierka.schronisko.authentication.AuthenticationUtils;
import pl.inzynierka.schronisko.common.ErrorResponse;
import pl.inzynierka.schronisko.common.SimpleResponse;
import pl.inzynierka.schronisko.shelters.ShelterServiceException;
import pl.inzynierka.schronisko.user.Role;
import pl.inzynierka.schronisko.user.User;

@RestController
@RequiredArgsConstructor
@RequestMapping("/animals")
@Tag(name = "Animals", description = "Provides data for animals")
public class AnimalController {
    private final AnimalService animalService;
    private final ModelMapper modelMapper;

    @GetMapping
    @PreAuthorize("hasAuthority('USER')")
    @Operation(
            summary = "get animals",
            description = "Gets paginated list of animals"
    )
    ResponseEntity<Page<AnimalResponse>> getAnimals(
            @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(animalService.getAnimals(pageable)
                                         .map(this::convertToResponse));
    }

    private AnimalResponse convertToResponse(Animal animal) {
        return modelMapper.map(animal, AnimalResponse.class);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('MODERATOR', 'ADMIN')")
    @Operation(summary = "create animal", description = "creates animal")
    ResponseEntity<AnimalResponse> createAnimal(
            @RequestBody @Validated AnimalRequest animal) throws
            InsufficentUserRoleException, AnimalServiceException {
        final User authenticatedUser =
                AuthenticationUtils.getAuthenticatedUser();

        if (authenticatedUser.hasNoRoles(Role.ADMIN) && !authenticatedUser.getShelter()
                .getName()
                .equals(animal.getShelter())) {
            throw new InsufficentUserRoleException(
                    "Użytkownik nie ma uprawnień do dodawania zwierząt do schroniska: " + animal.getShelter());
        }


        if (authenticatedUser.getRoles()
                .stream()
                .noneMatch(role -> Role.ADMIN.equals(role) || Role.MODERATOR.equals(
                        role))) {
            throw new InsufficentUserRoleException(
                    "User is not authorized to add animals!");
        }

        Animal newAnimal = animalService.createAnimal(animal,
                                                      authenticatedUser);

        return ResponseEntity.ok(convertToResponse(newAnimal));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('MODERATOR', 'ADMIN')")
    @Operation(summary = "update animal", description = "updates animal")
    ResponseEntity<AnimalResponse> updateAnimal(@RequestBody String request,
                                                @PathVariable long id) throws
            InsufficentUserRoleException, AnimalServiceException {
        final User authenticatedUser =
                AuthenticationUtils.getAuthenticatedUser();

        if (authenticatedUser.hasNoRoles(Role.ADMIN, Role.MODERATOR)) {
            throw new InsufficentUserRoleException(
                    "Uzytkownik nie ma uprawnien do tego requestu.");
        }
        return ResponseEntity.ok(convertToResponse(animalService.updateAnimal(id,
                                                                              request)));
    }

    @PostMapping("/search")
    @PreAuthorize("hasAnyAuthority('USER')")
    @Operation(summary = "Search for animals")
    ResponseEntity<Page<AnimalResponse>> findAnimals(
            @ParameterObject Pageable pageable,
            @RequestBody AnimalSearchQuery searchQuery) throws
            AnimalServiceException {
        Page<AnimalResponse> animals = animalService.searchForAnimals(
                searchQuery,
                pageable).map(this::convertToResponse);

        return ResponseEntity.ok(animals);
    }

    @ExceptionHandler(
            {AnimalServiceException.class,
             HttpMessageNotReadableException.class}
    )
    ResponseEntity<ErrorResponse> animalServiceException(Exception e,
                                                         WebRequest request) {
        return ResponseEntity.badRequest()
                .body(ErrorResponse.now(e.getMessage()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('MODERATOR', 'ADMIN')")
    ResponseEntity<SimpleResponse> deleteAnimal(@PathVariable long id) throws
            InsufficentUserRoleException {
        final User authenticatedUser =
                AuthenticationUtils.getAuthenticatedUser();
        Animal animalToDelete = animalService.getAnimalById(id)
                .orElseThrow(() -> new ShelterServiceException(
                        "Nie znaleziono zwierzęcia z takim id!"));

        if (authenticatedUser.hasNoRoles(Role.ADMIN) && animalToDelete.getShelter()
                .getName() != authenticatedUser.getShelter().getName()) {
            throw new InsufficentUserRoleException(
                    "Uzytkownik nie moze usuwac zwierząt ze schroniska w którym nie pracuje!");
        }

        if (authenticatedUser.hasNoRoles(Role.ADMIN, Role.MODERATOR)) {
            throw new InsufficentUserRoleException(
                    "Cannot delete animal if user is not at least moderator.");
        }

        return ResponseEntity.ok(animalService.deleteAnimal(id));
    }
}
