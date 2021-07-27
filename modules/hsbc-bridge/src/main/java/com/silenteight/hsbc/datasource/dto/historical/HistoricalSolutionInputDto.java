package com.silenteight.hsbc.datasource.dto.historical;

import lombok.Builder;
import lombok.Value;

import java.util.List;

import static java.util.Collections.emptyList;

@Builder
@Value
public class HistoricalSolutionInputDto {

  String match;
  @Builder.Default
  List<HistoricalFeatureInputDto> features = emptyList();
}
