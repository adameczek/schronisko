package pl.inzynierka.schronisko.favorites;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.inzynierka.schronisko.animals.Animal;

import java.util.List;
import java.util.Optional;

public interface FavoritesRepository extends JpaRepository<Favorite, Long> {
    @Query("SELECT favorites FROM Favorite WHERE user.id = ?1")
    List<Animal> findUserFavorites(long userId);
    
    @Query("SELECT DISTINCT(f) FROM Favorite f WHERE f.user.id = ?1")
    Optional<Favorite> findUserfavorites(long userId);
    
}
