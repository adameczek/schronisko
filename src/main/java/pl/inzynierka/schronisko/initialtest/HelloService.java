package pl.inzynierka.schronisko.initialtest;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class HelloService {
    private final HelloRepository helloRepository;

    public Mono<Hello> save(Hello hello) {
        return helloRepository.save(hello);
    }

    public Flux<Hello> findAll() {
        return helloRepository.findAll();
    }
}
