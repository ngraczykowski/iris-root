package com.silenteight.hsbc.bridge.match;

import lombok.Builder;
import lombok.Value;

import com.silenteight.hsbc.datasource.datamodel.MatchData;

@Builder
@Value
public class MatchComposite {

  Long id;
  String externalId;
  String name;
  MatchData matchData;
}
