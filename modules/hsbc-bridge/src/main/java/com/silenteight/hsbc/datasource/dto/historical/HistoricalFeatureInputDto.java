package com.silenteight.hsbc.datasource.dto.historical;

import lombok.Builder;
import lombok.Value;

import com.silenteight.hsbc.datasource.extractors.historical.ModelKeyDto.ModelKeyType;

import java.util.Collections;
import java.util.Map;

@Builder
@Value
public class HistoricalFeatureInputDto {

  String feature;
  int truePositiveCount;
  ModelKeyType modelKeyType;
  @Builder.Default
  Map<String, String> reason = Collections.emptyMap();
}
