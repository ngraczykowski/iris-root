package com.silenteight.adjudication.engine.alerts.match;

import lombok.Builder;
import lombok.NonNull;
import lombok.Singular;
import lombok.Value;

import com.silenteight.adjudication.api.v1.Match;

import java.util.List;

@Value
@Builder
public class NewAlertMatches {

  @NonNull
  String parentAlert;

  @NonNull
  @Singular
  List<Match> matches;
}
