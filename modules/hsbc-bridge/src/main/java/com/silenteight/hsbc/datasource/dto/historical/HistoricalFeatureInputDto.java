package com.silenteight.hsbc.datasource.dto.historical;

import lombok.Builder;
import lombok.Value;

import com.silenteight.hsbc.datasource.extractors.historical.ModelKeyDto.ModelKeyType;

import java.util.Map;

import static java.util.Collections.emptyMap;

@Builder
@Value
public class HistoricalFeatureInputDto {

  String feature;
  int truePositiveCount;
  ModelKeyType modelKeyType;
  @Builder.Default
  Map<String, String> reason = emptyMap();
}
