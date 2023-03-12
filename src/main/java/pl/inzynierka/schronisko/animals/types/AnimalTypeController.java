package pl.inzynierka.schronisko.animals.types;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.inzynierka.schronisko.animals.InsufficentUserRoleException;
import pl.inzynierka.schronisko.common.SimpleResponse;

@RestController
@RequiredArgsConstructor
@Tag(name = "Animal types")
@RequestMapping("/animals/types")
@Slf4j
public class AnimalTypeController {
  private final AnimalTypeService service;

  @GetMapping
  @PreAuthorize("hasAuthority('USER')")
  @Operation(
      summary = "Get types of animals",
      description = "Returns paginated list of animal tags")
  public ResponseEntity<Page<AnimalType>> getAllTypes(@ParameterObject Pageable pageable) {
    return ResponseEntity.ok(service.getAllTypes(pageable));
  }

  @PostMapping
  @PreAuthorize("hasAnyAuthority('MODERATOR', 'ADMIN')")
  @Operation(summary = "create animal type")
  public ResponseEntity<AnimalType> saveAnimalType(@RequestBody @Valid AnimalType animalType)
      throws InsufficentUserRoleException {
    log.info("Saving new animal type: {}", animalType);
    AnimalType createdAnimalType = service.saveAnimalType(animalType);
    log.info("Saving new animal type success! {}", createdAnimalType);

    return ResponseEntity.ok(createdAnimalType);
  }

  @DeleteMapping("/{value}")
  @PreAuthorize("hasAnyAuthority('MODERATOR', 'ADMIN')")
  @Operation(summary = "Delete animal type")
  public ResponseEntity<SimpleResponse> deleteType(@PathVariable String value)
      throws InsufficentUserRoleException {
    log.info("deleting tag with value: {}", value);

    SimpleResponse response = service.deleteAnimalType(value);

    if (response.isSuccess()) return ResponseEntity.ok(response);

    return ResponseEntity.notFound().build();
  }
}
