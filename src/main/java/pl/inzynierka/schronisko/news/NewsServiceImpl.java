package pl.inzynierka.schronisko.news;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.inzynierka.schronisko.shelters.ShelterRepository;
import pl.inzynierka.schronisko.user.User;
import pl.inzynierka.schronisko.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {
    private final NewsRepository newsRepository;
    private final UserRepository userRepository;
    private final ShelterRepository shelterRepository;
    private final ModelMapper modelMapper;
    @Override
    public Page<News> getNews(Pageable pageable) {
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), 25, Sort.Direction.DESC, "addedTime");
        return newsRepository.findAll(pageRequest);
    }
    
    public Page<News> getLastNewsFromShelter(String shelterName, int page) {
        PageRequest pageRequest = PageRequest.of(page, 10, Sort.Direction.DESC, "addedTime");
        
        return newsRepository.findAll(NewsSpecifications.belongsToShelter(shelterName), pageRequest);
    }
    
    @Override
    public News create(NewsRequest request, User authUser) {
        News news = modelMapper.map(request, News.class);
        news.setAddedBy(userRepository.getReferenceById(authUser.getId()));
        news.setShelter(shelterRepository.getReferenceById(authUser.getShelter().getId()));
        news.setAddedTime(LocalDateTime.now());
        
        return newsRepository.save(news);
    }
    
    @Override
    public Optional<News> getById(long id) {
        return newsRepository.findById(id);
    }
   
    
    @Override
    public boolean deleteById(long id) {
       newsRepository.deleteById(id);
       
       return true;
    }
}
