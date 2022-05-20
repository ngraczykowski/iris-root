package com.silenteight.adjudication.engine.common.grpc;

public class InvalidAnalysisException extends RuntimeException {

  private static final long serialVersionUID = -4592237300492464773L;

  public InvalidAnalysisException(String message) {
    super(message);
  }
}
