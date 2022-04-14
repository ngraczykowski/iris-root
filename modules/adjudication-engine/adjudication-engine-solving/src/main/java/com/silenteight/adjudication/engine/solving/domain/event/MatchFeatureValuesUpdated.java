package com.silenteight.adjudication.engine.solving.domain.event;


import java.time.Instant;
import java.util.UUID;

public class MatchFeatureValuesUpdated extends DomainBase {

  public static final String EVENT_TYPE = "match.feature.values.updated";

  public MatchFeatureValuesUpdated() {
    super(EVENT_TYPE, null, null, null);

  }

  public MatchFeatureValuesUpdated(final Long alertSolvingId) {
    super(EVENT_TYPE, Instant.now(), UUID.randomUUID(), alertSolvingId);
  }
}
