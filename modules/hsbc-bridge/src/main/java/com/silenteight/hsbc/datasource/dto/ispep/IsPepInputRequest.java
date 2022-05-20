package com.silenteight.hsbc.datasource.dto.ispep;

import lombok.Builder;
import lombok.Value;

import java.util.Collections;
import java.util.List;

@Builder
@Value
public class IsPepInputRequest {

  @Builder.Default
  List<String> matches = Collections.emptyList();
  @Builder.Default
  List<String> features = Collections.emptyList();
}
