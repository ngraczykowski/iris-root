package com.silenteight.hsbc.bridge.match;

import lombok.Value;

@Value
public class MatchIdComposite {

  long internalId;
  String externalId;
}
