package com.silenteight.hsbc.datasource.dto.historical;

import lombok.Builder;
import lombok.Value;

import java.util.List;

import static java.util.Collections.emptyList;

@Builder
@Value
public class HistoricalInputResponse {

  @Builder.Default
  List<HistoricalSolutionInputDto> solutions = emptyList();
}
