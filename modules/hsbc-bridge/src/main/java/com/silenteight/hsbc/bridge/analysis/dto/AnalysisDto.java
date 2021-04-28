package com.silenteight.hsbc.bridge.analysis.dto;

import lombok.Builder;
import lombok.ToString;
import lombok.Value;

@Builder
@Value
public class AnalysisDto {

  @ToString.Exclude
  Long id;
  String name;
  String policy;
  String strategy;
  long alertCount;
  String dataset;
}
