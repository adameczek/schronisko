package pl.inzynierka.schronisko.fileupload;

public class FileUploadServiceException extends RuntimeException {
    public FileUploadServiceException() {
    }
    
    public FileUploadServiceException(String message) {
        super(message);
    }
    
    public FileUploadServiceException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public FileUploadServiceException(Throwable cause) {
        super(cause);
    }
    
    public FileUploadServiceException(
            String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
