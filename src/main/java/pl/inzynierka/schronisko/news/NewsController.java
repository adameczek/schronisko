package pl.inzynierka.schronisko.news;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.inzynierka.schronisko.animals.InsufficentUserRoleException;
import pl.inzynierka.schronisko.authentication.AuthenticationUtils;
import pl.inzynierka.schronisko.common.SchroniskoException;
import pl.inzynierka.schronisko.common.SimpleResponse;
import pl.inzynierka.schronisko.user.User;

@RestController
@RequiredArgsConstructor
@RequestMapping("/news")
@Tag(
        name = "News",
        description = "Provides news"
)
public class NewsController {
    private final NewsService newsService;
    private final ModelMapper modelMapper;
    
    @GetMapping
    @Operation(
            summary = "Get last news",
            description = "Gets news sorted by creation date from latest to oldest"
    )
    ResponseEntity<Page<NewsResponse>> getNews(
            @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(newsService.getNews(pageable).map(this::convertToResponse));
    }
    
    @GetMapping("/{shelter}")
    @Operation(
            summary = "Get last news from shelter",
            description = "Gets last news from shelter"
    )
    ResponseEntity<Page<NewsResponse>> getLastNewsFromShelter(@PathVariable String shelter, @ParameterObject Pageable pageable)
    {
        Page<News> lastNews = newsService.getLastNewsFromShelter(shelter, pageable.getPageNumber());
        
        return ResponseEntity.ok(lastNews.map(this::convertToResponse));
    }
    
    @PostMapping
    @Operation(
            summary = "Create news",
            description = "Creates news"
    )
    @PreAuthorize("hasAnyAuthority('MODERATOR', 'SHELTER_OWNER')")
    ResponseEntity<NewsResponse> createNews(@RequestBody NewsRequest request) throws InsufficentUserRoleException {
        final User authUser = AuthenticationUtils.getAuthenticatedUser();
        
        if (authUser.getShelter() == null)
            throw new InsufficentUserRoleException("Nie można utworzyć newsa nie będąc pracownikiem schroniska!");
        
        return ResponseEntity.ok(convertToResponse(newsService.create(request, authUser)));
    }
    
    @DeleteMapping("/{id}")
    @Operation(
            summary = "delete news",
            description = "Deletes news with given id"
    )
    @PreAuthorize("hasAnyAuthority('MODERATOR', 'SHELTER_OWNER')")
    public ResponseEntity<SimpleResponse> deleteNews(@PathVariable long id)
            throws InsufficentUserRoleException, SchroniskoException {
        final User authUser = AuthenticationUtils.getAuthenticatedUser();
        
        News news = newsService.getById(id).orElseThrow(() -> new SchroniskoException("Nie ma newsa z podanym id!"));
        
        if (news.getShelter().equals(authUser.getShelter())) {
            return ResponseEntity.ok(new SimpleResponse(newsService.deleteById(id), null));
        } else {
            throw new InsufficentUserRoleException("Nie można usunąć newsa z nie swojego schroniska!");
        }
    }
    
    
    private NewsResponse convertToResponse(News news) {
        return modelMapper.map(news, NewsResponse.class);
    }
}
