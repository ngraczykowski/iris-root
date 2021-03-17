package com.silenteight.serp.governance.model.featureset;

import com.silenteight.serp.governance.model.NonResolvableResourceException;

public class NonResolvableFeatureSetException extends NonResolvableResourceException {

  private static final long serialVersionUID = 3878033022767764844L;

  NonResolvableFeatureSetException(String resourceName) {
    super("FeatureSet references resource that is not available: " + resourceName);
  }
}
