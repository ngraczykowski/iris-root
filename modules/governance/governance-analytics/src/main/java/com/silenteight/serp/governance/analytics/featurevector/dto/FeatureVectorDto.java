package com.silenteight.serp.governance.analytics.featurevector.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@AllArgsConstructor
@Builder
public class FeatureVectorDto {
  List<String> names;
  List<String> values;
}
