package pl.inzynierka.schronisko.configurations.security.jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "jwt")
@Configuration
@Data
public class JwtProperties {
    private String secretKey = "rzxlszyykpbgqcflzxsqcysyhljt";
    
    // validity in milliseconds
    private long validityInMs = 3600000; // 1h
}
