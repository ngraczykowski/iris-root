package com.silenteight.sep.filestorage.exception;

public class FileNotSavedException extends RuntimeException {

  private static final long serialVersionUID = 5720274640815792360L;

  public FileNotSavedException(String message, Throwable cause) {
    super(message, cause);
  }
}
