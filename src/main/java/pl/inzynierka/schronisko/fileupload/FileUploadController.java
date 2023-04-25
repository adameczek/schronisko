package pl.inzynierka.schronisko.fileupload;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.inzynierka.schronisko.authentication.AuthenticationUtils;
import pl.inzynierka.schronisko.imagescaler.ResolutionEnum;
import pl.inzynierka.schronisko.user.User;

import java.io.IOException;

@Controller
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileUploadController {
    private final FileUploadService fileUploadService;
    @PostMapping(value = "/upload")
    public ResponseEntity<ImageFileResponse> uploadFile(
            @RequestParam MultipartFile file) throws IOException {
        final User authenticatedUser = AuthenticationUtils.getAuthenticatedUser();
        ImageFileDTO imageFileDTO = fileUploadService.uploadImage(authenticatedUser, file);
        
        return ResponseEntity.ok(convertToResponse(imageFileDTO));
    }
    
    @GetMapping(
            value = "/{id}/{type}",
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
    )
    public ResponseEntity<byte[]> getImage(
            @PathVariable long id,
            @PathVariable String type) {
        ResolutionEnum resolutionEnum = ResolutionEnum.valueOf(type);
        
        return fileUploadService.getImage(id)
                                .map(imageFileDTO -> imageFileDTO.getImageByEnum(resolutionEnum))
                                .map(bytes -> bytes.length > 1 ?
                                              ResponseEntity.ok(bytes) :
                                              ResponseEntity.badRequest().body(bytes))
                                .orElse(ResponseEntity.badRequest().body(new byte[0]));
        
    }
    
    private ImageFileResponse convertToResponse(ImageFileDTO imageFileDTO) {
        final String baseUrl =
                ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        return new ImageFileResponse(imageFileDTO, baseUrl);
    }
}
