package com.silenteight.hsbc.bridge.match;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class MatchComposite {

  Long id;
  MatchRawData rawData;
}
