package com.silenteight.governance.api.library.v1.model;

public class GovernanceLibraryRuntimeException extends RuntimeException {

  private static final long serialVersionUID = -968416922680669894L;

  GovernanceLibraryRuntimeException(String message, Throwable cause) {
    super(message, cause);
  }
}
