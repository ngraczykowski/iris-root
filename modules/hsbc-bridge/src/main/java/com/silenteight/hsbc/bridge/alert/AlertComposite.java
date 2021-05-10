package com.silenteight.hsbc.bridge.alert;

import lombok.Value;

import com.silenteight.hsbc.bridge.match.Match;

import java.util.List;

@Value
public class AlertComposite {

  long id;
  String externalId;
  List<Match> matches;
}
