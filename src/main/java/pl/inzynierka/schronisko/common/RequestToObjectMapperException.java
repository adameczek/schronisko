package pl.inzynierka.schronisko.common;

public class RequestToObjectMapperException extends Throwable {
    public RequestToObjectMapperException() {
    }

    public RequestToObjectMapperException(String message) {
        super(message);
    }

    public RequestToObjectMapperException(String message, Throwable cause) {
        super(message, cause);
    }

    public RequestToObjectMapperException(Throwable cause) {
        super(cause);
    }

    public RequestToObjectMapperException(String message,
                                          Throwable cause,
                                          boolean enableSuppression,
                                          boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
