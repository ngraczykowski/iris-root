package com.silenteight.hsbc.datasource.extractors.historical;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class ModelCountsDto {

  ModelKeyDto modelKey;
  int truePositivesCount;
}
