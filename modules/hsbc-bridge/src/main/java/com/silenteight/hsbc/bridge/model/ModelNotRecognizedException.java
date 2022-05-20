package com.silenteight.hsbc.bridge.model;

import com.silenteight.hsbc.bridge.model.rest.input.ModelType;

class ModelNotRecognizedException extends RuntimeException {

  private static final long serialVersionUID = -5952313234534485573L;

  public ModelNotRecognizedException(String modelType) {
    super("Model type: " + modelType + " is not allowed. Allowed values are: " +
        ModelType.MODEL.name() + ", " +
        ModelType.IS_PEP_PROCEDURAL.name() + ", " +
        ModelType.IS_PEP_HISTORICAL.name() + ", " +
        ModelType.NAME_ALIASES.name());
  }
}
