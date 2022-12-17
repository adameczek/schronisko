package pl.inzynierka.schronisko.initialtest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class HelloWorldController {

    private final HelloService helloService;

    @GetMapping("/hello")
    private Flux<Hello> getHello() {
        return helloService.save(Hello.builder()
                .message(LocalDateTime.now().toString()).build()).thenMany(helloService.findAll());
    }
}
