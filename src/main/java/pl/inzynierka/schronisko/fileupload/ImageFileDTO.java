package pl.inzynierka.schronisko.fileupload;

import jakarta.persistence.Entity;
import pl.inzynierka.schronisko.imagescaler.ResolutionEnum;
import pl.inzynierka.schronisko.user.User;

import java.time.LocalDateTime;
import java.util.Map;

@Entity
public class ImageFileDTO extends FileDTO {
    private byte[] miniBytes;
    private byte[] mediumBytes;

    public ImageFileDTO(long id, byte[] bytes, User owner, LocalDateTime createdAt, byte[] miniBytes, byte[] mediumBytes) {
        super(id, bytes, owner, createdAt);
        this.miniBytes = miniBytes;
        this.mediumBytes = mediumBytes;
    }

    public ImageFileDTO(byte[] bytes, User owner, LocalDateTime createdAt, byte[] miniBytes, byte[] mediumBytes) {
        super(bytes, owner, createdAt);
        this.miniBytes = miniBytes;
        this.mediumBytes = mediumBytes;
    }
    
    public ImageFileDTO() {
        super();
    }
    
    public byte[] getMiniBytes() {
        return miniBytes;
    }

    public void setMiniBytes(byte[] miniBytes) {
        this.miniBytes = miniBytes;
    }

    public byte[] getMediumBytes() {
        return mediumBytes;
    }

    public void setMediumBytes(byte[] mediumBytes) {
        this.mediumBytes = mediumBytes;
    }
    
    public static ImageFileDTO fromMapWithImages(User owner, Map<ResolutionEnum, byte[]> imageMap) {
        ImageFileDTO imageFileDTO = new ImageFileDTO();
        imageFileDTO.setOwner(owner);
        imageFileDTO.setCreatedAt(LocalDateTime.now());
        
        imageMap.forEach((resolutionEnum, bytes) -> {
            switch (resolutionEnum){
                case HIGH -> {
                   imageFileDTO.setBytes(bytes);
                }
                case MEDIUM -> {
                    imageFileDTO.setMediumBytes(bytes);
                }
                case MINI -> {
                    imageFileDTO.setMiniBytes(bytes);
                }
            }
        });
        
    }
}
