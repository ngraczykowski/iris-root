package com.silenteight.hsbc.datasource.dto.historical;

import lombok.Builder;
import lombok.Value;

import java.util.Collections;
import java.util.List;

@Builder
@Value
public class HistoricalSolutionInputDto {

  String match;
  @Builder.Default
  List<HistoricalFeatureInputDto> features = Collections.emptyList();
}
