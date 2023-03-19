package pl.inzynierka.schronisko.animals;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import pl.inzynierka.schronisko.common.ErrorResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/animals")
public class AnimalController {
  private final AnimalService animalService;

  @GetMapping
  @PreAuthorize("hasAuthority('USER')")
  @Operation(summary = "get animals", description = "Gets paginated list of animals")
  ResponseEntity<Page<Animal>> getAnimals(@ParameterObject Pageable pageable) {
    return ResponseEntity.ok(animalService.getAnimals(pageable));
  }

  @PostMapping
  @PreAuthorize("hasAnyAuthority('MODERATOR', 'ADMIN')")
  @Operation(summary = "create animal", description = "creates animal")
  ResponseEntity<Animal> createAnimal(
          @RequestBody @Validated AnimalRequest animal)
          throws InsufficentUserRoleException, AnimalServiceException {
    Animal newAnimal = animalService.createAnimal(animal);

    return ResponseEntity.ok(newAnimal);
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasAnyAuthority('MODERATOR', 'ADMIN')")
  @Operation(summary = "update animal", description = "updates animal")
  ResponseEntity<Animal> updateAnimal(
          @RequestBody String request, @PathVariable long id)
          throws InsufficentUserRoleException, AnimalServiceException {
    return ResponseEntity.ok(animalService.updateAnimal(id, request));
  }

  @PostMapping("/search")
  @PreAuthorize("hasAnyAuthority('USER')")
  @Operation(summary = "Search for animals")
  ResponseEntity<Page<Animal>> findAnimals(@ParameterObject Pageable pageable,
                                           @RequestBody AnimalSearchQuery searchQuery) {
    animalService.searchForAnimals(searchQuery, pageable);

    return ResponseEntity.internalServerError().build();
  }

  @ExceptionHandler(
          {AnimalServiceException.class,
           HttpMessageNotReadableException.class}
  )
  ResponseEntity<ErrorResponse> animalServiceException(
          Exception e, WebRequest request) {
    return ResponseEntity.badRequest().body(ErrorResponse.now(e.getMessage()));
  }
}
