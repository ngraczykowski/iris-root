package com.silenteight.hsbc.bridge.match;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class MatchComposite {

  long id;
  MatchRawData rawData;
}
