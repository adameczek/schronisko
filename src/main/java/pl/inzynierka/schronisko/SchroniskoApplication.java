package pl.inzynierka.schronisko;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@SpringBootApplication
@EnableReactiveMongoRepositories
@EnableConfigurationProperties
public class SchroniskoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SchroniskoApplication.class, args);
	}
}