package pl.inzynierka.schronisko.fileupload;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/files")
public class FileUploadController {
    @PostMapping("/upload")
    public ResponseEntity<Void> uploadFile(@RequestParam MultipartFile file) throws IOException {
        
        
        return ResponseEntity.ok(null);
    }
}
