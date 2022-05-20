package com.silenteight.adjudication.engine.solving.domain.event;

import lombok.AllArgsConstructor;

import com.silenteight.adjudication.engine.solving.domain.AlertSolving;

@AllArgsConstructor
public class FeatureMatchesUpdated extends DomainBase {

  public static final String EVENT_TYPE = "feature.matches.updated";
  private static final long serialVersionUID = -1507287639967520519L;

  public FeatureMatchesUpdated(final AlertSolving alertSolving) {
    super(alertSolving, EVENT_TYPE);
  }
}
