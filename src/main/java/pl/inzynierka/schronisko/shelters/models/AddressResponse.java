package pl.inzynierka.schronisko.shelters.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddressResponse {
    private String postCode;
    private String city;
    private String voivodeship;
    private String street;
    private String houseNumber;
    private Float longitude;
    private Float latitude;
}
