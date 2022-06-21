package com.silenteight.serp.governance.model.grpc.exception;

public class InvalidModelDeployedOnProductionRequestException extends RuntimeException {

  private static final long serialVersionUID = 848447329262011290L;

  public InvalidModelDeployedOnProductionRequestException() {
    super("The Model Deployed On Production request contains no model name nor version.");
  }
}
