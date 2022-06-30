package com.silenteight.agent.common.metrics;

import lombok.NonNull;
import lombok.Value;

@Value
public class MetricTag {

  @NonNull
  String name;
  @NonNull
  String value;
}
