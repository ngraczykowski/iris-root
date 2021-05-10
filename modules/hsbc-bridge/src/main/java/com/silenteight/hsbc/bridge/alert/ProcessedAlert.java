package com.silenteight.hsbc.bridge.alert;

import lombok.Value;

import com.silenteight.hsbc.bridge.match.Match;

import java.util.List;

@Value
class ProcessedAlert {

  String externalId;
  List<Match> matches;
}
