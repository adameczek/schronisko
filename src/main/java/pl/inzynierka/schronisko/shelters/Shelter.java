package pl.inzynierka.schronisko.shelters;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.inzynierka.schronisko.animals.Animal;
import pl.inzynierka.schronisko.user.User;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Shelter {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(unique = true, nullable = false)
    private String name;
    @OneToMany
    private List<User> employees;
    @OneToMany
    private List<Animal> animals;
    @Size(min = 0, max = 5000)
    private String description;
    @OneToOne
    @JoinColumn(unique = true, nullable = false)
    private Address location;
}
