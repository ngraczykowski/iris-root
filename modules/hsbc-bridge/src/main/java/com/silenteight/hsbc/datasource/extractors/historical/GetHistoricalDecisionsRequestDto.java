package com.silenteight.hsbc.datasource.extractors.historical;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class GetHistoricalDecisionsRequestDto {

  List<ModelKeyDto> modelKeys;
}
