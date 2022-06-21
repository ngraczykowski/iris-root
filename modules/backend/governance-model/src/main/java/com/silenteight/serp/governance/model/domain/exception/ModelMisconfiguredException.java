package com.silenteight.serp.governance.model.domain.exception;

public class ModelMisconfiguredException extends RuntimeException {

  private static final long serialVersionUID = 7078175203285168490L;

  public ModelMisconfiguredException(String modelName, String field) {
    super("Some elements of " + modelName + " are not configured: " + field);
  }
}
