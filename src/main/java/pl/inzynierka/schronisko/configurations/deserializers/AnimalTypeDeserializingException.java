package pl.inzynierka.schronisko.configurations.deserializers;

public class AnimalTypeDeserializingException extends DeserializingException {
    public AnimalTypeDeserializingException() {
    }

    public AnimalTypeDeserializingException(String message) {
        super(message);
    }

    public AnimalTypeDeserializingException(String message, Throwable cause) {
        super(message, cause);
    }

    public AnimalTypeDeserializingException(Throwable cause) {
        super(cause);
    }
}
