package pl.inzynierka.schronisko.advices;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import pl.inzynierka.schronisko.shelters.models.Shelter;
import pl.inzynierka.schronisko.user.User;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Advice {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @NotNull
    @ManyToOne
    private User addedBy;
    @NotNull
    @ManyToOne
    private Shelter shelter;
    @NotNull
    private LocalDateTime addedTime;
    @NotNull
    @Size(min = 0, max = 5000)
    private String message;
    @NotNull
    @Size(min = 1, max = 300)
    private String title;
    
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        
        Advice advice = (Advice) o;
        
        return id == advice.id;
    }
    
    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
