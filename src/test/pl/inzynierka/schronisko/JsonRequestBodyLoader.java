package pl.inzynierka.schronisko;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@RequiredArgsConstructor
@Component
public class JsonRequestBodyLoader {
    public String getRequestFromFile(String key, String fileName) throws IOException {
        var ob = new ObjectMapper();
        File resource = new ClassPathResource("requests/" + fileName + ".json").getFile();
        
        JsonNode node = ob.readValue(resource, JsonNode.class);
        
        return node.get(key).toString();
    }
    
}
