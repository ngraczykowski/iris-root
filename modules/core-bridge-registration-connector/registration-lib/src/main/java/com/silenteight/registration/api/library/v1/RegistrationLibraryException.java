package com.silenteight.registration.api.library.v1;

import java.io.Serial;

class RegistrationLibraryException extends RuntimeException {

  @Serial private static final long serialVersionUID = -8376244079979593798L;

  public RegistrationLibraryException(String message, Throwable cause) {
    super(message, cause);
  }
}
