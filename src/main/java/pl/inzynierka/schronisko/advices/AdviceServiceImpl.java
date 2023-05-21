package pl.inzynierka.schronisko.advices;

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
public class AdviceServiceImpl implements AdviceService {
    private final AdviceRepository adviceRepository;
    private final UserRepository userRepository;
    private final ShelterRepository shelterRepository;
    private final ModelMapper modelMapper;
    @Override
    public Page<Advice> getNews(Pageable pageable) {
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), 25, Sort.Direction.DESC, "addedTime");
        return adviceRepository.findAll(pageRequest);
    }
    
    public Page<Advice> getLastNewsFromShelter(String shelterName, int page) {
        PageRequest pageRequest = PageRequest.of(page, 10, Sort.Direction.DESC, "addedTime");
        
        return adviceRepository.findAll(AdviceSpecifications.belongsToShelter(shelterName), pageRequest);
    }
    
    @Override
    public Advice create(AdviceRequest request, User authUser) {
        Advice advice = modelMapper.map(request, Advice.class);
        advice.setAddedBy(userRepository.getReferenceById(authUser.getId()));
        advice.setShelter(shelterRepository.getReferenceById(authUser.getShelter().getId()));
        advice.setAddedTime(LocalDateTime.now());
        
        return adviceRepository.save(advice);
    }
    
    @Override
    public Optional<Advice> getById(long id) {
        return adviceRepository.findById(id);
    }
   
    
    @Override
    public boolean deleteById(long id) {
       adviceRepository.deleteById(id);
       
       return true;
    }
}
