package com.silenteight.serp.governance.model.featureset;

import lombok.NonNull;
import lombok.Value;

@Value
class FeatureJson {

  @NonNull
  String name;
  @NonNull
  String agentConfig;
}
