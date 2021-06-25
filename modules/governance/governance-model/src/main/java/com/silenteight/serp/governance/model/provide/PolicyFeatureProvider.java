package com.silenteight.serp.governance.model.provide;

import com.silenteight.model.api.v1.Feature;

import java.util.List;

public interface PolicyFeatureProvider {

  List<Feature> resolveFeatures(List<String> conditions);
}
