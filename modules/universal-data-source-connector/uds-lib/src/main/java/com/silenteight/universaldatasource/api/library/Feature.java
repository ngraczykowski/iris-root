package com.silenteight.universaldatasource.api.library;

import com.silenteight.datasource.agentinput.api.v1.FeatureInput;

public interface Feature {

  void accept(FeatureBuilderProvider provider, FeatureInput.Builder builder);
}
