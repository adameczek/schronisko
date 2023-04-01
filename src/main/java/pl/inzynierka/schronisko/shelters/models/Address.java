package pl.inzynierka.schronisko.shelters.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(nullable = false)
    @NotNull
    @Size(min = 6, max = 6)
    @Schema(description = "Post code", example = "12-345")
    private String postCode;
    @Column(nullable = false)
    @Size(min = 1, max = 100)
    @NotNull
    private String city;
    @Column(nullable = false)
    @Size(min = 1, max = 100)
    private String voivodeship;
    @Column(nullable = false)
    @NotNull
    @Size(min = 1, max = 100)
    private String street;
    @Size(min = 0, max = 100)
    private String houseNumber;
}
