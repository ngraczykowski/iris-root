package com.silenteight.sens.webapp.backend.circuitbreaker;

class InvalidBranchIdException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  InvalidBranchIdException() {
  }

  InvalidBranchIdException(Throwable e) {
    super(e);
  }
}
