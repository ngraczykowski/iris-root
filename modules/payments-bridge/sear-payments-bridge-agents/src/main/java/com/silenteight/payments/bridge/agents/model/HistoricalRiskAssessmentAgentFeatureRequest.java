package com.silenteight.payments.bridge.agents.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
//NOTE(jgajewski): Remove 'Feature' from class name 'HistoricalRiskAssessmentAgentFeatureRequest',
// when category use case is going to be deleted, after successful testing
public class HistoricalRiskAssessmentAgentFeatureRequest {

  String feature;

  String matchId;

  String alertedPartyId;

  String ofacId;

  String watchlistType;
}
