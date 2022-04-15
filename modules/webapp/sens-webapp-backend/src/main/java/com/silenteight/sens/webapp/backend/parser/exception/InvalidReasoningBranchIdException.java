package com.silenteight.sens.webapp.backend.parser.exception;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class InvalidReasoningBranchIdException extends RuntimeException {

  private static final long serialVersionUID = 2776652055950242546L;

  private final String branchId;

  public InvalidReasoningBranchIdException(String branchId, Throwable e) {
    super(e);
    this.branchId = branchId;
  }
}
