package pl.inzynierka.schronisko.shelters;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.inzynierka.schronisko.shelters.models.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {}
