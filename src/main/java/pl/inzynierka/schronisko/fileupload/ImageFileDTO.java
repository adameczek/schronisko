package pl.inzynierka.schronisko.fileupload;

import pl.inzynierka.schronisko.user.User;

import java.time.LocalDateTime;

public class ImageFileDTO extends FileDTO {
    private byte[] smallBytes;
    private byte[] mediumBytes;

    public ImageFileDTO(long id, byte[] bytes, User owner, LocalDateTime createdAt, byte[] smallBytes, byte[] mediumBytes) {
        super(id, bytes, owner, createdAt);
        this.smallBytes = smallBytes;
        this.mediumBytes = mediumBytes;
    }

    public ImageFileDTO(byte[] bytes, User owner, LocalDateTime createdAt, byte[] smallBytes, byte[] mediumBytes) {
        super(bytes, owner, createdAt);
        this.smallBytes = smallBytes;
        this.mediumBytes = mediumBytes;
    }

    public byte[] getSmallBytes() {
        return smallBytes;
    }

    public void setSmallBytes(byte[] smallBytes) {
        this.smallBytes = smallBytes;
    }

    public byte[] getMediumBytes() {
        return mediumBytes;
    }

    public void setMediumBytes(byte[] mediumBytes) {
        this.mediumBytes = mediumBytes;
    }
}
