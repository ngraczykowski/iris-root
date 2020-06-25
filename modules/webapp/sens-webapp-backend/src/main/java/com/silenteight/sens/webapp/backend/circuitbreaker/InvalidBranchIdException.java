package com.silenteight.sens.webapp.backend.circuitbreaker;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
class InvalidBranchIdException extends RuntimeException {

  private static final long serialVersionUID = 2776652055950242546L;

  private final String branchId;

  InvalidBranchIdException(String branchId, Throwable e) {
    super(e);
    this.branchId = branchId;
  }
}
