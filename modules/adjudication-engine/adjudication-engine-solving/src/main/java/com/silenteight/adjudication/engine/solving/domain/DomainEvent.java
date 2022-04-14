package com.silenteight.adjudication.engine.solving.domain;

import com.silenteight.adjudication.engine.solving.domain.event.FeatureMatchesUpdated;
import com.silenteight.adjudication.engine.solving.domain.event.MatchFeatureValuesUpdated;
import com.silenteight.adjudication.engine.solving.domain.event.MatchesUpdated;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "eventType")
@JsonSubTypes({
    @Type(value = FeatureMatchesUpdated.class, name = FeatureMatchesUpdated.EVENT_TYPE),
    @Type(value = MatchesUpdated.class, name = MatchesUpdated.EVENT_TYPE),
    @Type(value = MatchFeatureValuesUpdated.class, name = MatchFeatureValuesUpdated.EVENT_TYPE)
})
public interface DomainEvent extends Serializable {

  String eventType();

  Instant occurredOn();

  UUID id();

  Long alertSolvingId();
}
