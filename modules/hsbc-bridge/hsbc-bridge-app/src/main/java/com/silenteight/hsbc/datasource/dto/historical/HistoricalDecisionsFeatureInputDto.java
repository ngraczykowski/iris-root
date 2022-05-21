package com.silenteight.hsbc.datasource.dto.historical;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class HistoricalDecisionsFeatureInputDto {

  String feature;
  ModelKeyDto modelKey;
  String discriminator;
}
