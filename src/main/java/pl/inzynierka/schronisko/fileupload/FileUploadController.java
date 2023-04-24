package pl.inzynierka.schronisko.fileupload;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import pl.inzynierka.schronisko.authentication.AuthenticationUtils;
import pl.inzynierka.schronisko.user.User;

import java.io.IOException;

@Controller
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileUploadController {
    private final FileUploadService fileUploadService;
    @PostMapping("/upload")
    public ResponseEntity<ImageFileDTO> uploadFile(@RequestParam MultipartFile file) throws IOException {
        
        final User authenticatedUser = AuthenticationUtils.getAuthenticatedUser();
        ImageFileDTO imageFileDTO = fileUploadService.uploadImage(authenticatedUser, file);
        
        return ResponseEntity.ok(imageFileDTO);
    }
    
    
}
