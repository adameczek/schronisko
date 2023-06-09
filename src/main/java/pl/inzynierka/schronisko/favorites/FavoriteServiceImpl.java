package pl.inzynierka.schronisko.favorites;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.inzynierka.schronisko.animals.Animal;
import pl.inzynierka.schronisko.user.User;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {
    private final FavoritesRepository favoritesRepository;
    
    @Override
    public List<Animal> getUserFavorites(User user) {
        return favoritesRepository.findUserFavorites(user.getId());
    }
    
    @Override
    public void removeAnimalFromFavorites(Animal animal, User user) {
        removeAnimalFromFavorites(animal.getId(), user.getId());
    }
    
    @Override
    @Transactional
    public void removeAnimalFromFavorites(long animalId, long userId) {
        Optional<Favorite> userfavorites = favoritesRepository.findUserfavorites(userId);
        
        if (userfavorites.isEmpty())
            return;
        
        Set<Animal> updatedFavorites = userfavorites.get()
                                                    .getFavorites()
                                                    .stream()
                                                    .filter(animal -> animal.getId()
                                                                      != animalId)
                                                    .collect(Collectors.toSet());
        
        userfavorites.get().setFavorites(updatedFavorites);
        
        favoritesRepository.save(userfavorites.get());
    }
    
    @Override
    @Transactional
    public void addAnimalToFavorites(User user, Animal animal) {
        Favorite userFavorites = favoritesRepository.findUserfavorites(user.getId()).orElse(new Favorite(0l,
                                                                                                         user,
                                                                                                         new HashSet<>()));
        userFavorites.getFavorites().add(animal);
        
        favoritesRepository.save(userFavorites);
    }
}
