package com.silenteight.adjudication.engine.solving.domain.event;


import lombok.AllArgsConstructor;

import com.silenteight.adjudication.engine.solving.domain.AlertSolving;

@AllArgsConstructor
public class MatchFeatureValuesUpdated extends DomainBase {

  public static final String EVENT_TYPE = "match.feature.values.updated";

  public MatchFeatureValuesUpdated(final AlertSolving alertSolving) {
    super(alertSolving, EVENT_TYPE);
  }
}
