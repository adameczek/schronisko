package pl.inzynierka.schronisko.shelters;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.inzynierka.schronisko.animals.Animal;
import pl.inzynierka.schronisko.animals.AnimalService;
import pl.inzynierka.schronisko.common.RequestToObjectMapper;
import pl.inzynierka.schronisko.common.RequestToObjectMapperException;
import pl.inzynierka.schronisko.common.SimpleResponse;
import pl.inzynierka.schronisko.shelters.models.Address;
import pl.inzynierka.schronisko.shelters.models.Shelter;
import pl.inzynierka.schronisko.shelters.models.ShelterRequest;
import pl.inzynierka.schronisko.user.User;
import pl.inzynierka.schronisko.user.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShelterServiceImpl implements ShelterService {
    private final ShelterRepository repository;
    private final AnimalService animalService;
    private final AddressRepository addressRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;
    
    @Override
    public Page<Shelter> getShelters(Pageable pageable) {
        return repository.findAll(pageable);
    }
    
    @Override
    public Optional<Shelter> getShelter(String name) {
        return repository.findFirstByName(name);
    }
    
    @Override
    public Shelter createShelter(ShelterRequest request) {
        User owner = userService.findByEmail(request.getOwnerEmail())
                                .orElseThrow(() -> new ShelterServiceException(
                                        "Uzytkownik z podanym mailem nie został znaleziony!"));
        
        
        Shelter shelterToSave = modelMapper.map(request, Shelter.class);
        shelterToSave.setOwner(owner);
        Address savedAddress = addressRepository.save(shelterToSave.getAddress());
        shelterToSave.setAddress(savedAddress);
        
        log.info("Saving new shelter with name: {}", request.getName());
        return repository.save(shelterToSave);
    }
    
    @Override
    public Shelter updateShelter(ShelterRequest request) {
        Shelter savedShelter = repository.findFirstByName(request.getName())
                                         .orElseThrow(() -> new ShelterServiceException(
                                                 "Nie odnaleziono schroniska z podaną nazwą!"));
        
        try {
            var modelMapperForUpdate = new ModelMapper();
            modelMapperForUpdate.getConfiguration().setSkipNullEnabled(true);
            modelMapperForUpdate.map(request, savedShelter);
            savedShelter.setUpdatedAt(LocalDateTime.now());
            
            return repository.save(savedShelter);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Updating shelter failed! Data for shelter update: {}", request);
            throw new ShelterServiceException(e);
        }
    }
    
    @Override
    public Shelter updateShelter(JsonNode request, String shelterName) {
        Shelter savedShelter = repository.findFirstByName(shelterName)
                                         .orElseThrow(() -> new ShelterServiceException(
                                                 "Nie odnaleziono schroniska z podaną nazwą!"));
        
        try {
            Shelter updatedShelter = RequestToObjectMapper.mapRequestToObjectForUpdate(request,
                                                                                       savedShelter,
                                                                                       ShelterRequest.class,
                                                                                       Shelter.class,
                                                                                       modelMapper);
            if (updatedShelter.getAddress() == null)
                throw new ShelterServiceException("Adres nie może być pusty!");
            
            addressRepository.save(updatedShelter.getAddress());
            return repository.save(updatedShelter);
        } catch (RequestToObjectMapperException e) {
            log.error("Error occured while trying to update {}", shelterName);
            e.printStackTrace();
            throw new ShelterServiceException("Wystąpił problem przy aktualizowaniu danych schroniska!");
        }
    }
    
    @Override
    public SimpleResponse deleteShelter(String name) {
        long result = repository.deleteByName(name);
        
        log.info("Deleting shelter by name: {} result: {}", name, result);
        
        return new SimpleResponse(result == 1L, null);
    }
    
    @Override
    public SimpleResponse addEmployee(String name, List<String> userEmails) {
        Shelter shelter = repository.findFirstByName(name).orElseThrow(() -> new ShelterServiceException(
                "Nie odnaleziono schroniska z podaną nazwą!"));
        
        List<User> users = userEmails.stream()
                                     .filter(Objects::nonNull)
                                     .map(userEmail -> userService.findByEmail(userEmail)
                                                                  .orElseThrow(() -> new ShelterServiceException(
                                                                          "Nie odnaleziono użytkownika z mailem: "
                                                                          + userEmail)))
                                     .toList();
        
        shelter.getEmployees().addAll(users);
        
        try {
            repository.save(shelter);
        } catch (DataIntegrityViolationException e) {
            throw new ShelterServiceException("Użytkownik już jest pracownikiem w jakimś schronisku!");
        }
        
        return new SimpleResponse(true, null);
    }
    
    @Override
    @Transactional
    public SimpleResponse removeEmployeeFromShelter(String name, String email) {
        Shelter shelter = repository.findFirstByName(name).orElseThrow(() -> new ShelterServiceException(
                "Nie odnaleziono schroniska z podaną nazwą!"));
        
        List<User> updatedList = shelter.getEmployees()
                                        .stream()
                                        .filter(user -> !user.getEmail().equals(email))
                                        .collect(Collectors.toCollection(ArrayList::new));
        
        if (updatedList.size() == shelter.getEmployees().size())
            return new SimpleResponse(false,
                                      "Nie znaleziono użytkownika z mailem: "
                                      + email
                                      + " na liście pracowników schroniska.");
        
        shelter.setEmployees(updatedList);
        
        repository.save(shelter);
        
        return new SimpleResponse(true, null);
    }
    
    @Override
    public SimpleResponse addAnimal(String shelterName, List<Integer> animalIds) {
        Shelter shelter = repository.findFirstByName(shelterName).orElseThrow(() -> new ShelterServiceException(
                "Nie odnaleziono schroniska z podaną nazwą!"));
        
        List<Animal> animalsToAdd = animalIds.stream()
                                             .filter(Objects::nonNull)
                                             .map(id -> animalService.getAnimalById(id)
                                                                     .orElseThrow(() -> new ShelterServiceException(
                                                                             "Nie znaleziono zwierzęcia z id: "
                                                                             + id)))
                                             .toList();
        
        shelter.getAnimals().addAll(animalsToAdd);
        try {
            repository.save(shelter);
        } catch (DataIntegrityViolationException e) {
            throw new ShelterServiceException("Zwierzęcie już jest w jakimś schronisku!");
        }
        
        
        return new SimpleResponse(true, null);
    }
    
    @Override
    @Transactional
    public SimpleResponse removeAnimal(String shelterName, int id) {
        Shelter shelter = repository.findFirstByName(shelterName).orElseThrow(() -> new ShelterServiceException(
                "Nie odnaleziono schroniska z podaną nazwą!"));
        
        List<Animal> updatedAnimalsList = shelter.getAnimals()
                                                 .stream()
                                                 .filter(animal -> animal.getId() != id)
                                                 .collect(Collectors.toCollection(ArrayList::new));
        
        if (updatedAnimalsList.size() == shelter.getAnimals().size())
            return new SimpleResponse(false, "nie znaleziono zwierzęcia z podanym id: " + id + " w schronisku.");
        
        shelter.setAnimals(updatedAnimalsList);
        repository.save(shelter);
        
        return new SimpleResponse(true, null);
    }
}
