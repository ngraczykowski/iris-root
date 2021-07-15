package com.silenteight.hsbc.bridge.model.dto;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class SolvingModelDto {

  String name;
  String policyName;
  String strategyName;
  List<FeatureDto> features;
  List<String> categories;
}
