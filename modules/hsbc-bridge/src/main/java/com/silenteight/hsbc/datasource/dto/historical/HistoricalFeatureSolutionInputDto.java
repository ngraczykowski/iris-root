package com.silenteight.hsbc.datasource.dto.historical;

import lombok.Builder;
import lombok.Value;

import java.util.Map;

@Builder
@Value
public class HistoricalFeatureSolutionInputDto {

  String solution;
  Map<String, String> reason;
}
