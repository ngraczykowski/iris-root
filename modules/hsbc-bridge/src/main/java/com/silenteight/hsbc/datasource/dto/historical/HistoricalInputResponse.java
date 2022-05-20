package com.silenteight.hsbc.datasource.dto.historical;

import lombok.Builder;
import lombok.Value;

import java.util.Collections;
import java.util.List;

@Value
@Builder
public class HistoricalInputResponse {

  @Builder.Default
  List<HistoricalDecisionsInputDto> inputs = Collections.emptyList();
}
