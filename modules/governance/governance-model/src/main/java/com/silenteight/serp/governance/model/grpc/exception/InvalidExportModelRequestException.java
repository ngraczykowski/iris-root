package com.silenteight.serp.governance.model.grpc.exception;

public class InvalidExportModelRequestException extends RuntimeException {

  private static final long serialVersionUID = -4796758196333979011L;

  public InvalidExportModelRequestException() {
    super("The export model request contains no model name nor version.");
  }
}
