package com.silenteight.hsbc.datasource.dto.historical;

import lombok.Builder;
import lombok.Value;

import java.util.Collections;
import java.util.List;

@Builder
@Value
public class HistoricalInputResponse {

  @Builder.Default
  List<HistoricalSolutionInputDto> inputs = Collections.emptyList();
}
