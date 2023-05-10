package pl.inzynierka.schronisko.shelters.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.inzynierka.schronisko.animals.AnimalResponse;
import pl.inzynierka.schronisko.user.UserResponse;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ShelterResponse {
    private String name;
    private List<UserResponse> employees;
    private List<AnimalResponse> animals;
    private String description;
    private AddressResponse address;
    private String phoneNumber;
    private String email;
}
