package pl.inzynierka.schronisko.favorites;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.inzynierka.schronisko.animals.Animal;
import pl.inzynierka.schronisko.animals.AnimalResponse;
import pl.inzynierka.schronisko.authentication.AuthenticationUtils;
import pl.inzynierka.schronisko.user.User;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/favorites")
public class FavoriteController {
    private final FavoriteService favoriteService;
    private final ModelMapper modelMapper;
    
    @PostMapping
    @PreAuthorize("hasAnyAuthority('USER')")
    public ResponseEntity<Void> addAnimalToFavorites(
            @RequestParam long animalId) {
        User authenticatedUser = AuthenticationUtils.getAuthenticatedUser();
        favoriteService.addAnimalToFavorites(authenticatedUser, Animal.builder().id(animalId).build());
        
        return ResponseEntity.ok(null);
    }
    
    @GetMapping
    @PreAuthorize("hasAnyAuthority('USER')")
    public ResponseEntity<List<AnimalResponse>> getUserFavorites() {
        User authenticatedUser = AuthenticationUtils.getAuthenticatedUser();
        
        List<AnimalResponse> favorites = favoriteService.getUserFavorites(authenticatedUser)
                                                        .stream()
                                                        .map((element) -> modelMapper.map(element,
                                                                                          AnimalResponse.class))
                                                        .collect(Collectors.toList());
        return ResponseEntity.ok(favorites);
        
    }
    
    @DeleteMapping
    @PreAuthorize("hasAnyAuthority('USER')")
    public ResponseEntity<Void> removeAnimalFromFavorites(
            @RequestParam long animalId) {
        User authenticatedUser = AuthenticationUtils.getAuthenticatedUser();
        
        favoriteService.removeAnimalFromFavorites(animalId, authenticatedUser.getId());
        
        return ResponseEntity.ok(null);
    }
}
