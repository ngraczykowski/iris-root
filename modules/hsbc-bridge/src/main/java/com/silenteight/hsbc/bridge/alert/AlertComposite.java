package com.silenteight.hsbc.bridge.alert;

import lombok.Builder;
import lombok.Value;

import com.silenteight.hsbc.bridge.match.Match;

import java.util.List;

@Builder
@Value
public class AlertComposite {

  long id;
  String externalId;
  boolean invalid;
  List<Match> matches;
}
