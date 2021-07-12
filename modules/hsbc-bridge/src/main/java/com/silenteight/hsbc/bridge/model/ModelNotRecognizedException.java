package com.silenteight.hsbc.bridge.model;

import static com.silenteight.hsbc.bridge.model.rest.input.ModelType.IS_PEP_HISTORICAL;
import static com.silenteight.hsbc.bridge.model.rest.input.ModelType.IS_PEP_PROCEDURAL;
import static com.silenteight.hsbc.bridge.model.rest.input.ModelType.MODEL;
import static com.silenteight.hsbc.bridge.model.rest.input.ModelType.NAME_ALIASES;

class ModelNotRecognizedException extends RuntimeException {

  private static final long serialVersionUID = -5952313234534485573L;

  public ModelNotRecognizedException(String modelType) {
    super("Model type: " + modelType + " is not allowed. Allowed values are: " +
        MODEL.name() + ", " +
        IS_PEP_PROCEDURAL.name() + ", " +
        IS_PEP_HISTORICAL.name() + ", " +
        NAME_ALIASES.name());
  }
}
