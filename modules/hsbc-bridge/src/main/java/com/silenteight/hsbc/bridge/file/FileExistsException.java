package com.silenteight.hsbc.bridge.file;

class FileExistsException extends RuntimeException {

  public FileExistsException(String message) {
    super(message);
  }
}
