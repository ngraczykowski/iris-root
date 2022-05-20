package com.silenteight.adjudication.engine.solving.domain.event;

import lombok.AllArgsConstructor;

import com.silenteight.adjudication.engine.solving.domain.AlertSolving;

@AllArgsConstructor
public class MatchesUpdated extends DomainBase {

  public static final String EVENT_TYPE = "matches.updated";

  public MatchesUpdated(final AlertSolving alertSolving) {
    super(alertSolving,  EVENT_TYPE);
  }
}
