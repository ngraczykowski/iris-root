package com.silenteight.serp.governance.model.featureset;

import lombok.NonNull;
import lombok.Value;

import java.util.List;

@Value
class FeatureSetJson {

  @NonNull
  String name;
  @NonNull
  List<FeatureJson> features;
}
