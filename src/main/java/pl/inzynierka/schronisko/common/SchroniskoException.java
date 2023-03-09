package pl.inzynierka.schronisko.common;

public class SchroniskoException extends Exception {
  public SchroniskoException() {}

  public SchroniskoException(String message) {
    super(message);
  }

  public SchroniskoException(String message, Throwable cause) {
    super(message, cause);
  }

  public SchroniskoException(Throwable cause) {
    super(cause);
  }

  public SchroniskoException(
      String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
