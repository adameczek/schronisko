package pl.inzynierka.schronisko.configurations.converters;

import org.modelmapper.AbstractConverter;
import pl.inzynierka.schronisko.fileupload.ImageFileDTO;
import pl.inzynierka.schronisko.fileupload.ImageFileResponse;

import java.util.List;

public class ImageListConverter extends AbstractConverter<List<ImageFileDTO>, List<ImageFileResponse>> {
    private final String appUrl;
    
    public ImageListConverter(String appUrl) {this.appUrl = appUrl;}
    
    @Override
    public List<ImageFileResponse> convert(List<ImageFileDTO> imageFileDTOS) {
        
        return imageFileDTOS.stream().map(imageFileDTO -> new ImageFileResponse(imageFileDTO, appUrl)).toList();
    }
}
