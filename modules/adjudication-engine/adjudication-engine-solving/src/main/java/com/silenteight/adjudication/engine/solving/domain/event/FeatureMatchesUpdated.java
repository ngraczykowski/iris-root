package com.silenteight.adjudication.engine.solving.domain.event;

import java.time.Instant;
import java.util.UUID;

public class FeatureMatchesUpdated extends DomainBase {

  public static final String EVENT_TYPE = "feature.matches.updated";

  public FeatureMatchesUpdated() {
    super(EVENT_TYPE, null, null, null);
  }

  public FeatureMatchesUpdated(final Long alertSolvingId) {
    super(EVENT_TYPE, Instant.now(), UUID.randomUUID(), alertSolvingId);
  }
}
