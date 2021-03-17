package com.silenteight.serp.governance.model.featureset;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class FeatureDto {

  @NonNull
  String name;
  @NonNull
  String agentConfig;
  @NonNull
  List<String> values;
}
