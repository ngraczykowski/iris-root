package com.silenteight.serp.governance.file.validation.exception;

import lombok.Getter;

import com.silenteight.serp.governance.file.validation.Error;

public class InvalidFileException extends RuntimeException {

  private static final long serialVersionUID = 2132399276936428703L;

  @Getter
  private final Error error;

  public InvalidFileException(String message, Error error) {
    super(message);
    this.error = error;
  }
}
