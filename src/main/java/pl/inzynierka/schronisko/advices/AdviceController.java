package pl.inzynierka.schronisko.advices;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.inzynierka.schronisko.animals.InsufficentUserRoleException;
import pl.inzynierka.schronisko.authentication.AuthenticationUtils;
import pl.inzynierka.schronisko.common.SchroniskoException;
import pl.inzynierka.schronisko.common.SimpleResponse;
import pl.inzynierka.schronisko.user.User;

@RestController
@RequiredArgsConstructor
@RequestMapping("/advices")
@Tag(
        name = "Advices",
        description = "Provides advices"
)
public class AdviceController {
    private final AdviceService adviceService;
    private final ModelMapper modelMapper;
    
    @GetMapping
    @Operation(
            summary = "Get last Advices",
            description = "Gets Advices sorted by creation date from latest to oldest"
    )
    ResponseEntity<Page<AdviceResponse>> getNews(
            @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(adviceService.getNews(pageable).map(this::convertToResponse));
    }
    
    @GetMapping("/{shelter}")
    @Operation(
            summary = "Get last advices from shelter",
            description = "Gets last advices from shelter"
    )
    ResponseEntity<Page<AdviceResponse>> getLastNewsFromShelter(@PathVariable String shelter, @ParameterObject Pageable pageable)
    {
        Page<Advice> lastNews = adviceService.getLastNewsFromShelter(shelter, pageable.getPageNumber());
        
        return ResponseEntity.ok(lastNews.map(this::convertToResponse));
    }
    
    @PostMapping
    @Operation(
            summary = "Create advice",
            description = "Creates advice"
    )
    @PreAuthorize("hasAnyAuthority('MODERATOR', 'SHELTER_OWNER')")
    ResponseEntity<AdviceResponse> createNews(@RequestBody AdviceRequest request) throws InsufficentUserRoleException {
        final User authUser = AuthenticationUtils.getAuthenticatedUser();
        
        if (authUser.getShelter() == null)
            throw new InsufficentUserRoleException("Nie można utworzyć porady nie będąc pracownikiem schroniska!");
        
        return ResponseEntity.ok(convertToResponse(adviceService.create(request, authUser)));
    }
    
    @DeleteMapping("/{id}")
    @Operation(
            summary = "delete advice",
            description = "Deletes advice with given id"
    )
    @PreAuthorize("hasAnyAuthority('MODERATOR', 'SHELTER_OWNER')")
    public ResponseEntity<SimpleResponse> deleteNews(@PathVariable long id)
            throws InsufficentUserRoleException, SchroniskoException {
        final User authUser = AuthenticationUtils.getAuthenticatedUser();
        
        Advice advice = adviceService.getById(id).orElseThrow(() -> new SchroniskoException("Nie ma porady z podanym id!"));
        
        if (advice.getShelter().equals(authUser.getShelter())) {
            return ResponseEntity.ok(new SimpleResponse(adviceService.deleteById(id), null));
        } else {
            throw new InsufficentUserRoleException("Nie można usunąć porady z nie swojego schroniska!");
        }
    }
    
    
    private AdviceResponse convertToResponse(Advice advice) {
        return modelMapper.map(advice, AdviceResponse.class);
    }
}
