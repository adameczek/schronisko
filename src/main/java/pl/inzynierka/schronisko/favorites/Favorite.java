package pl.inzynierka.schronisko.favorites;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import pl.inzynierka.schronisko.animals.Animal;
import pl.inzynierka.schronisko.user.User;

import java.util.LinkedHashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @OneToOne(optional = false)
    private User user;
    
    @ManyToMany(cascade = CascadeType.DETACH)
    @JoinTable(name = "favorite_favorites",
               joinColumns = @JoinColumn(name = "favorite_id"),
               inverseJoinColumns = @JoinColumn(name = "favorites_id")
    
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Animal> favorites = new LinkedHashSet<>();
    
}

