package pl.inzynierka.schronisko.initialtest;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HelloService {
	private final HelloRepository helloRepository;

	public Hello save(final Hello hello) {
		return this.helloRepository.save(hello);
	}

	public List<Hello> findAll() {
		return this.helloRepository.findAll();
	}
}
