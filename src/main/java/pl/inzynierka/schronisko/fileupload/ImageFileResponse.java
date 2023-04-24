package pl.inzynierka.schronisko.fileupload;

import io.swagger.v3.oas.annotations.media.Schema;

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
}
