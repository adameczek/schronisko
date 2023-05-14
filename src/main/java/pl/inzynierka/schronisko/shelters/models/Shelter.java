package pl.inzynierka.schronisko.shelters.models;

import com.fasterxml.jackson.annotation.JsonMerge;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
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
    @LastModifiedDate
    LocalDateTime updatedAt;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(
            unique = true,
            nullable = false
    )
    private String name;
    @OneToOne
    private User owner;
    @OneToMany(fetch = FetchType.LAZY)
    private List<User> employees;
    @Size(
            min = 0,
            max = 5000
    )
    private String description;
    @OneToMany(fetch = FetchType.LAZY)
    private List<Animal> animals;
    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(
            unique = true,
            nullable = false
    )
    @JsonMerge
    private Address address;
    @Email(message = "Email is not valid")
    @Schema(
            description = "Email of user",
            example = "robert@kubica.pl"
    )
    @Column(
            unique = true,
            nullable = false
    )
    private String email;
    @Size(min = 9, max = 9)
    @Nullable
    private String phoneNumber;
    
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        
        Shelter shelter = (Shelter) o;
        
        return id == shelter.id;
    }
    
    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
