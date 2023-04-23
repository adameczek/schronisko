package pl.inzynierka.schronisko.user;

import pl.inzynierka.schronisko.common.SchroniskoException;

public class UserServiceException extends SchroniskoException {
    public UserServiceException() {
    }
    
    public UserServiceException(String message) {
        super(message);
    }
    
    public UserServiceException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public UserServiceException(Throwable cause) {
        super(cause);
    }
    
    public UserServiceException(
            String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
