package pl.inzynierka.schronisko.fileupload;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.inzynierka.schronisko.imagescaler.CroppedImage;
import pl.inzynierka.schronisko.imagescaler.ImageTools;
import pl.inzynierka.schronisko.imagescaler.ResolutionEnum;
import pl.inzynierka.schronisko.user.User;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FileUploadService {
    private final ImageFileRepository imageFileRepository;
    
    public ImageFileDTO uploadImage(User creator, MultipartFile file) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(file.getInputStream());
        } catch (IOException e) {
            throw new FileUploadServiceException(e);
        }
        
        CroppedImage croppedImage = ImageTools.cropImageToNearestResolution(image);
        
        Map<ResolutionEnum, byte[]> imageMap = new HashMap<>();
        
        for (ResolutionEnum resolution : ResolutionEnum.values()) {
            byte[] imageBytes;
            try {
                if (resolution.equals(croppedImage.resolution())) {
                    imageBytes = ImageTools.bufferedImageToByteArray(croppedImage.image(), "jpg");
                } else {
                    CroppedImage resized = ImageTools.resize(croppedImage.image(), resolution);
                    imageBytes = ImageTools.bufferedImageToByteArray(resized.image(), "jpg");
                }
                imageMap.put(resolution, imageBytes);
            } catch (IOException e) {
                throw new FileUploadServiceException(e);
            }
        }
        
        ImageFileDTO imageFileDTO = ImageFileDTO.fromMapWithImages(creator, imageMap);
        
        return imageFileRepository.save(imageFileDTO);
    }
    
    public Optional<ImageFileDTO> getImage(long id) {
        return imageFileRepository.findById(id);
    }
    
    public InputStream getImageByIdAndResolution(long id, ResolutionEnum resolutionEnum) {
        ImageFileDTO imageFileDTO = imageFileRepository.findById(id)
                                                       .orElseThrow(() -> new FileUploadServiceException(String.format("File with id: %d not found!",
                                                                                                                       id)));
        
        return new ByteArrayInputStream(imageFileDTO.getImageByEnum(resolutionEnum));
    }
    
    public boolean removeImageById(Long id) {
        try {
            imageFileRepository.deleteById(id);
            
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            
            return false;
        }
    }
}
