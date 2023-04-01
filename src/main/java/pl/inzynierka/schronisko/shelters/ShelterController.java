package pl.inzynierka.schronisko.shelters;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import pl.inzynierka.schronisko.authentication.AuthenticationUtils;
import pl.inzynierka.schronisko.common.ErrorResponse;
import pl.inzynierka.schronisko.common.SimpleResponse;
import pl.inzynierka.schronisko.shelters.models.Shelter;
import pl.inzynierka.schronisko.shelters.models.ShelterRequest;
import pl.inzynierka.schronisko.shelters.models.ShelterResponse;
import pl.inzynierka.schronisko.user.Role;
import pl.inzynierka.schronisko.user.User;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/shelters")
@Tag(name = "shelters", description = "Provides shelter data")
public class ShelterController {
    private final ShelterService shelterService;
    private final ModelMapper modelMapper;

    @GetMapping
    @PreAuthorize("hasAuthority('USER')")
    @Operation(
            summary = "Get shelters", description = "Gets list of shelters"
    )
    ResponseEntity<Page<ShelterResponse>> getShelters(Pageable pageable) {
        return ResponseEntity.ok(shelterService.getShelters(pageable)
                                         .map(this::convertToResponse));
    }

    private ShelterResponse convertToResponse(Shelter shelter) {
        return modelMapper.map(shelter, ShelterResponse.class);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(
            summary = "Create shelter",
            description = "Creates a shelter and returns it"
    )
    @ApiResponses(
            value = {@ApiResponse(
                    responseCode = "200",
                    description = "Shelter created",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ShelterResponse.class)
                    )}
            )
            }
    )
    ResponseEntity<ShelterResponse> createShelter(@Valid @RequestBody
                                                  ShelterRequest request) {
        return ResponseEntity.ok(convertToResponse(shelterService.createShelter(
                request)));
    }

    @GetMapping("/{name}")
    @PreAuthorize("hasAuthority('USER')")
    @Operation(
            summary = "Get shelter",
            description = "Gets shelter by name or returns bad request if not found"
    )
    ResponseEntity<ShelterResponse> getShelterByName(
            @PathVariable String name) {
        return shelterService.getShelter(name).map(this::convertToResponse).map(
                ResponseEntity::ok).orElse(ResponseEntity.badRequest().build());
    }

    @PutMapping("/{name}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(
            summary = "Update Shelter",
            description = "Updates shelter with given data"
    )
    ResponseEntity<ShelterResponse> updateShelter(@RequestBody JsonNode request,
                                                  @PathVariable String name) {
        return ResponseEntity.ok(convertToResponse(shelterService.updateShelter(
                request,
                name)));
    }

    @DeleteMapping("/{name}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(
            summary = "Delete shelter", description = "Deletes shelter by name"
    )
    ResponseEntity<SimpleResponse> deleteShelter(@PathVariable String name) {
        return ResponseEntity.ok(shelterService.deleteShelter(name));
    }

    @PostMapping("/{name}/employees")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SHELTER_OWNER')")
    @Operation(
            summary = "Add employees", description = "Adds employees to shelter"
    )
    ResponseEntity<SimpleResponse> addEmployeeToShelter(
            @PathVariable String name, @RequestBody List<String> userEmails) {
        return ResponseEntity.ok(shelterService.addEmployee(name, userEmails));
    }

    @DeleteMapping("/{name}/employees/{email}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SHELTER_OWNER')")
    @Operation(
            summary = "Remove employee from shelter",
            description = "Removes employee with given email from shelter"
    )
    ResponseEntity<SimpleResponse> removeEmployeeFromShelter(
            @PathVariable String name, @PathVariable String email) {
        User authenticatedUser = AuthenticationUtils.getAuthenticatedUser();

        if (authenticatedUser.hasAnyRole(Role.ADMIN) || authenticatedUser.getShelter()
                .getName()
                .equals(name)) {
            return ResponseEntity.ok(shelterService.removeEmployeeFromShelter(
                    name,
                    email));
        } else {
            return ResponseEntity.badRequest().body(new SimpleResponse(false,
                                                                       "Nie masz uprawnień do usunięcia pracownika z tego schroniska."));
        }
    }

    @PostMapping("/{name}/animals")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    @Operation(
            summary = "Add animal to shelter",
            description = "Removes animal from shelter"
    )
    ResponseEntity<SimpleResponse> addAnimalToShelter(@PathVariable String name,
                                                      @RequestBody
                                                      @NotNull List<Integer> id) {
        User requester = AuthenticationUtils.getAuthenticatedUser();

        if (requester.hasNoRoles(Role.ADMIN) && !requester.getShelter()
                .getName()
                .equals(name)) throw new ShelterServiceException(
                "Użytkownik nie jest uprawniony do dodawania zwierząt do schroniska: " + name);

        return ResponseEntity.ok(shelterService.addAnimal(name, id));
    }

    @DeleteMapping("/{name}/animals/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    @Operation(
            summary = "Remove animal from shelter",
            description = "Removes animal from shelter"
    )
    ResponseEntity<SimpleResponse> removeAnimalFromShelter(
            @PathVariable String name, @PathVariable String id) {
        User requester = AuthenticationUtils.getAuthenticatedUser();
        if (requester.hasNoRoles(Role.ADMIN) && !requester.getShelter()
                .getName()
                .equals(name)) throw new ShelterServiceException(
                "Użytkownik nie jest uprawniony do dodawania zwierząt do schroniska: " + name);

        return ResponseEntity.ok(shelterService.removeAnimal(name,
                                                             Integer.parseInt(id)));
    }

    @ExceptionHandler({ShelterServiceException.class})
    ResponseEntity<ErrorResponse> shelterServiceException(Exception e,
                                                          WebRequest request) {
        log.error(
                "Shelter service exception occured for request!\nDetails: {}, request details: {}",
                e.getMessage(),
                request.getDescription(true));

        return ResponseEntity.badRequest()
                .body(ErrorResponse.now(e.getMessage()));
    }
}
