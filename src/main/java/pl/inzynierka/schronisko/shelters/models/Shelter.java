package pl.inzynierka.schronisko.shelters.models;

import com.fasterxml.jackson.annotation.JsonMerge;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;
import pl.inzynierka.schronisko.animals.Animal;
import pl.inzynierka.schronisko.user.User;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Setter
public class Shelter {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(unique = true, nullable = false)
    private String name;
    @LastModifiedDate
    LocalDateTime updatedAt;
    @OneToOne
    private User owner;
    @OneToMany(fetch = FetchType.LAZY)
    private List<User> employees;
    @Size(min = 0, max = 5000)
    private String description;
    @OneToMany(fetch = FetchType.LAZY)
    private List<Animal> animals;
    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(unique = true, nullable = false)
    @JsonMerge
    private Address address;
}
