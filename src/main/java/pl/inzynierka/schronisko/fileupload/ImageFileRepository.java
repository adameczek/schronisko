package pl.inzynierka.schronisko.fileupload;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageFileRepository extends JpaRepository<ImageFileDTO, Long> {

}
