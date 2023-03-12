package pl.inzynierka.schronisko.animals.tags;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
@RequestMapping("/animals/tags")
@Tag(name = "Animal tags", description = "Provides animal tags data")
@Slf4j
public class AnimalTagController {
    private final AnimalTagService service;

  @GetMapping
  @PreAuthorize("hasAuthority('USER')")
  @Operation(
      summary = "Gets tags",
      description = "Gets paginated animal tags",
      tags = {"animals", "tags"})
  public ResponseEntity<Page<AnimalTag>> getAllTags(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok(service.getAllTags(pageable));
    }

  @PostMapping
  @PreAuthorize("hasAnyAuthority('MODERATOR', 'ADMIN')")
  @Operation(summary = "Creates animal tag")
  public ResponseEntity<AnimalTag> saveTag(@RequestBody @Valid AnimalTag animalTag)
      throws InsufficentUserRoleException, AnimalTagServiceException {
    log.info("Saving new tag: {}", animalTag.toString());
    AnimalTag newAnimalTag = service.saveTag(animalTag);
    log.info("Saving new tag {} success!", newAnimalTag);
    return ResponseEntity.ok(newAnimalTag);
  }

  @DeleteMapping("/{value}")
  @PreAuthorize("hasAnyAuthority('MOODERATOR', 'ADMIN')")
  @Operation(summary = "Deletes tag")
  public ResponseEntity<SimpleResponse> deleteTag(@PathVariable String value)
      throws InsufficentUserRoleException {
        log.info("Deleting tag with value: {}", value);

        SimpleResponse response = service.deleteTag(value);

        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
