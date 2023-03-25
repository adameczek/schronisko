package pl.inzynierka.schronisko.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SimpleResponse {
    private boolean success;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String additionalInfo;
}
