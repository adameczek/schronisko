package pl.inzynierka.schronisko.favorites;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.inzynierka.schronisko.animals.Animal;
import pl.inzynierka.schronisko.user.User;

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
    @OneToMany
    private Set<Animal> favorites;
}

