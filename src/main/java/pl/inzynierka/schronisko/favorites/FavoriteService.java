package pl.inzynierka.schronisko.favorites;

import pl.inzynierka.schronisko.animals.Animal;
import pl.inzynierka.schronisko.user.User;

import java.util.List;

public interface FavoriteService {
    List<Animal> getUserFavorites(User user);
    void removeAnimalFromFavorites(Animal animal, User user);
    void removeAnimalFromFavorites(long animalId, long userId);
    void addAnimalToFavorites(User user, Animal animal);
}
