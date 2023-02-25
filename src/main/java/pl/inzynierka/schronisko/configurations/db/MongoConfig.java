package pl.inzynierka.schronisko.configurations.db;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.FindAndModifyOptions;


@Configuration
public class MongoConfig {
    @Bean
    public FindAndModifyOptions findAndModifyOptions() {
        return FindAndModifyOptions.options().returnNew(true);
    }
}
