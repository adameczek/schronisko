package pl.inzynierka.schronisko.advices;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.inzynierka.schronisko.user.User;

import java.util.Optional;

public interface AdviceService {
    Page<Advice> getNews(Pageable pageable);
    
    Page<Advice> getLastNewsFromShelter(String shelter, int page);
    
    Advice create(AdviceRequest request, User authUser);
    
    Optional<Advice> getById(long id);
    
    boolean deleteById(long id);
}
