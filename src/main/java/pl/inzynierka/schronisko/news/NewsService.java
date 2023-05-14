package pl.inzynierka.schronisko.news;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.inzynierka.schronisko.user.User;

import java.util.Optional;

public interface NewsService {
    Page<News> getNews(Pageable pageable);
    
    Page<News> getLastNewsFromShelter(String shelter, int page);
    
    News create(NewsRequest request, User authUser);
    
    Optional<News> getById(long id);
    
    boolean deleteById(long id);
}
