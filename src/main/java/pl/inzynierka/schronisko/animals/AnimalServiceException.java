package pl.inzynierka.schronisko.animals;

import pl.inzynierka.schronisko.common.SchroniskoException;

public class AnimalServiceException extends SchroniskoException {
    public AnimalServiceException(String s) {
        super(s);
    }
    
    public AnimalServiceException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public AnimalServiceException(Throwable cause) {
        super(cause);
    }
    
    public AnimalServiceException(
            String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
