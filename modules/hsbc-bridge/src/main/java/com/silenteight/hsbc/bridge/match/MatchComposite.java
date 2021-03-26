package com.silenteight.hsbc.bridge.match;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotNull;

@Builder
@Value
public class MatchComposite {

  Long id;
  @NotNull
  String name;
  MatchRawData rawData;
}
