package pl.inzynierka.schronisko.configurations.deserializers;

import java.io.IOException;

public class DeserializingException extends IOException {
    public DeserializingException() {
    }
    
    public DeserializingException(String message) {
        super(message);
    }
    
    public DeserializingException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public DeserializingException(Throwable cause) {
        super(cause);
    }
}
