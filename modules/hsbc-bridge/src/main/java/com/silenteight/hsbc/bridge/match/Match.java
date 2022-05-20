package com.silenteight.hsbc.bridge.match;

import lombok.Value;

import com.silenteight.hsbc.bridge.json.external.model.HsbcMatch;

@Value
public class Match {

  String externalId;
  HsbcMatch matchData;
}
