package com.silenteight.sens.webapp.backend.reasoningbranch.list.exception;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class InvalidBranchIdException extends RuntimeException {

  private static final long serialVersionUID = 17296509957309708L;

  private final String branchId;

  public InvalidBranchIdException(String branchId, Throwable e) {
    super(e);
    this.branchId = branchId;
  }
}
