package pl.inzynierka.schronisko.fileupload;

import jakarta.persistence.*;
import pl.inzynierka.schronisko.user.User;

import java.time.LocalDateTime;

@Entity
public class FileDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private byte[] bytes;
    @ManyToOne
    private pl.inzynierka.schronisko.user.User owner;
    private LocalDateTime createdAt;

    public FileDTO(long id, byte[] bytes, User owner, LocalDateTime createdAt) {
        this.id = id;
        this.bytes = bytes;
        this.owner = owner;
        this.createdAt = createdAt;
    }

    public FileDTO(byte[] bytes, User owner, LocalDateTime createdAt) {
        this.bytes = bytes;
        this.owner = owner;
        this.createdAt = createdAt;
    }

    public FileDTO() {

    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
