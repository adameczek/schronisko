package pl.inzynierka.schronisko.configurations;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@OpenAPIDefinition(
        info = @Info(
                title = "Schronisko API",
                description = "Provides access to schronisko resources",
                contact = @Contact(
                        name = "Adam Sawicki",
                        email = "asawicki@cdv.edu.pl"
                )
        )
)
@SecurityScheme(
        name = "api",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfiguration {
}
