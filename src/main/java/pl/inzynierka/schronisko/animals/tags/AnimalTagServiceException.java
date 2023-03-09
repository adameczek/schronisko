package pl.inzynierka.schronisko.animals.tags;

import pl.inzynierka.schronisko.common.SchroniskoException;

public class AnimalTagServiceException extends SchroniskoException {
  public AnimalTagServiceException(String message) {
    super(message);
  }

  public AnimalTagServiceException(Exception e) {
    super(e);
  }
}
