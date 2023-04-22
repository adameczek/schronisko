package pl.inzynierka.schronisko;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableConfigurationProperties
@EnableJpaRepositories
@ConfigurationPropertiesScan
public class SchroniskoApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(SchroniskoApplication.class, args);
    }
}