package com.silenteight.sens.webapp.grpc;

import lombok.Data;

@Data
public class InvalidBranchSolutionException extends RuntimeException {

  private final String solution;

  public InvalidBranchSolutionException(String solution, Throwable cause) {
    super(cause);
    this.solution = solution;
  }
}
