package com.silenteight.serp.governance.changerequest.domain.exception;

import lombok.NonNull;

import static java.lang.String.format;

public class ModelApprovalNotFoundException extends RuntimeException {

  private static final long serialVersionUID = -6452697830321565251L;

  public ModelApprovalNotFoundException(@NonNull String modelName) {
    super(format("Model Approval for model=%s not found.", modelName));
  }
}
