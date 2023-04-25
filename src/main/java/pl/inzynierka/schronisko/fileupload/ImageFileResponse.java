package pl.inzynierka.schronisko.fileupload;

import io.swagger.v3.oas.annotations.media.Schema;
import pl.inzynierka.schronisko.imagescaler.ResolutionEnum;

import java.util.HashMap;
import java.util.Map;

public class ImageFileResponse {
    private final long id;
    @Schema(description = "Map of image sise and url pointing to it")
    private final Map<String, String> images;
    
    public ImageFileResponse(long id, Map<String, String> images) {
        this.id = id;
        this.images = images;
    }
    
    public long getId() {
        return id;
    }
    
    public Map<String, String> getImages() {
        return images;
    }
    
    public ImageFileResponse(ImageFileDTO imageFileDTO, String appUrl) {
        this.id = imageFileDTO.getId();
        
        final Map<String, String> imageMap = new HashMap<>();
        for (ResolutionEnum resolutionEnum : ResolutionEnum.values()) {
            String value = resolutionEnum.name().toLowerCase();
            
            imageMap.put(value, appUrl + "/files/" + imageFileDTO.getId() + "/" + value);
        }
        
        this.images = imageMap;
    }
}
