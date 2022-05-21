package com.silenteight.hsbc.datasource.dto.historical;

import lombok.Builder;
import lombok.Value;

import java.util.Collections;
import java.util.List;

@Value
@Builder
public class HistoricalDecisionsInputDto {

  String match;
  @Builder.Default
  List<HistoricalDecisionsFeatureInputDto> features = Collections.emptyList();
}
