package com.silenteight.hsbc.bridge.analysis.dto;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class CreateAnalysisRequestDto {

  String name;
  String policy;
  String strategy;
  List<String> categories;
  List<FeatureDto> features;
}
