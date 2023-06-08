package pl.inzynierka.schronisko.configurations.converters;

import org.modelmapper.AbstractConverter;
import pl.inzynierka.schronisko.fileupload.FileDTO;
import pl.inzynierka.schronisko.fileupload.ImageFileDTO;

import java.util.List;

public class ImageListToRequestConverter extends AbstractConverter<List<ImageFileDTO>, List<Long>> {
    @Override
    protected List<Long> convert(List<ImageFileDTO> imageFileDTOList) {
        return imageFileDTOList.stream().map(FileDTO::getId).toList();
    }
}
