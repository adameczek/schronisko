package pl.inzynierka.schronisko.shelters;

public class ShelterServiceException extends RuntimeException {
    public ShelterServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ShelterServiceException(Throwable cause) {
        super(cause);
    }

    public ShelterServiceException(String message,
                                   Throwable cause,
                                   boolean enableSuppression,
                                   boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ShelterServiceException(String s) {
        super(s);
    }
}
