package com.silenteight.sep.filestorage.exception;

public class FileNotFoundException extends RuntimeException {

  private static final long serialVersionUID = 5440274360815792740L;

  public FileNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
