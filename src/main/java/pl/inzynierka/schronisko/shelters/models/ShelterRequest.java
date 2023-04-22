package pl.inzynierka.schronisko.shelters.models;

import com.fasterxml.jackson.annotation.JsonMerge;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ShelterRequest {
    @NotNull
    @Size(
            min = 1,
            max = 100
    )
    private String name;
    @Size(
            min = 0,
            max = 5000
    )
    private String description;
    @NotNull
    @JsonMerge
    private Address address;
    @NotNull
    private String ownerEmail;
}
