package pl.inzynierka.schronisko.configurations.appsettings;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "settings.user-service")
@AllArgsConstructor
@Getter
public class UserServiceSettings {
    private final List<String> editableFields;
}
