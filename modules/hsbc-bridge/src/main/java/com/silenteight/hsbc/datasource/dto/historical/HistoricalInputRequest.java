package com.silenteight.hsbc.datasource.dto.historical;

import lombok.Builder;
import lombok.Value;

import java.util.Collections;
import java.util.List;

@Builder
@Value
public class HistoricalInputRequest {

  @Builder.Default
  List<String> matches = Collections.emptyList();
  @Builder.Default
  List<String> features = Collections.emptyList();
}
