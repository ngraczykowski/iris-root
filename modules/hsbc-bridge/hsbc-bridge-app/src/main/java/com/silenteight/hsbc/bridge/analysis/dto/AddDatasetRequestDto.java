package com.silenteight.hsbc.bridge.analysis.dto;


import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class AddDatasetRequestDto {

  String analysis;
  String dataset;
}
