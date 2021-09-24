package com.silenteight.serp.governance.file.validation.exception;

import java.io.IOException;

public class FileBrokenContentException extends RuntimeException {

  private static final long serialVersionUID = 1238164203285398490L;

  public FileBrokenContentException(String message, IOException e) {
    super(message, e);
  }
}
