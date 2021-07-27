package com.silenteight.hsbc.datasource.dto.historical;

import lombok.Builder;
import lombok.Value;

import java.util.List;

import static java.util.Collections.emptyList;

@Builder
@Value
public class HistoricalFeatureInputDto {

  String feature;
  @Builder.Default
  List<HistoricalFeatureSolutionInputDto> featureSolutions = emptyList();
}
