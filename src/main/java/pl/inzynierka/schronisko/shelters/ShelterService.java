package pl.inzynierka.schronisko.shelters;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.inzynierka.schronisko.common.SimpleResponse;
import pl.inzynierka.schronisko.shelters.models.Shelter;
import pl.inzynierka.schronisko.shelters.models.ShelterRequest;

import java.util.List;
import java.util.Optional;

public interface ShelterService {

    Page<Shelter> getShelters(Pageable pageable);

    Optional<Shelter> getShelter(String name);

    Shelter createShelter(ShelterRequest request);

    Shelter updateShelter(ShelterRequest request);

    Shelter updateShelter(JsonNode request, String shelterName);

    SimpleResponse deleteShelter(String name);

    SimpleResponse addEmployee(String name, List<String> userEmails);

    SimpleResponse removeEmployeeFromShelter(String name, String email);

    SimpleResponse addAnimal(String shelterName, List<Integer> animalIds);

    SimpleResponse removeAnimal(String shelterName, int id);
}
