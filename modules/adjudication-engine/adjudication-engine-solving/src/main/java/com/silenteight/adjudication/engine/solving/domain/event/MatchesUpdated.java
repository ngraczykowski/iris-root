package com.silenteight.adjudication.engine.solving.domain.event;

import java.time.Instant;
import java.util.UUID;

public class MatchesUpdated extends DomainBase {

  public static final String EVENT_TYPE = "feature.matches.updated";

  public MatchesUpdated() {
    super(EVENT_TYPE, null, null, null);

  }

  public MatchesUpdated(final Long alertSolvingId) {
    super(EVENT_TYPE, Instant.now(), UUID.randomUUID(), alertSolvingId);
  }
}
