package com.silenteight.serp.governance.model.defaultmodel.grpc;

class ModelMisconfiguredException extends Exception {

  private static final long serialVersionUID = 7078175203285168490L;

  ModelMisconfiguredException(String modelName, String field) {
    super("Some elements of " + modelName + " are not configured: " + field);
  }
}
