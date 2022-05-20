package com.silenteight.adjudication.engine.solving.domain.event;

import com.silenteight.adjudication.engine.solving.domain.AlertSolving;

public class MatchFeatureUpdated extends DomainBase {

  public static final String EVENT_TYPE = "match.feature.updated";

  public MatchFeatureUpdated(
      final AlertSolving alertSolving
  ) {
    super(alertSolving, EVENT_TYPE);
  }
}
