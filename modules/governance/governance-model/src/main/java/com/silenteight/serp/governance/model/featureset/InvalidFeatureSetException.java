package com.silenteight.serp.governance.model.featureset;

import com.silenteight.serp.governance.model.NonResolvableResourceException;

public class InvalidFeatureSetException extends NonResolvableResourceException {

  private static final long serialVersionUID = 3878033022767764844L;

  InvalidFeatureSetException(String message) {
    super("FeatureSet definition is invalid: " + message);
  }
}
