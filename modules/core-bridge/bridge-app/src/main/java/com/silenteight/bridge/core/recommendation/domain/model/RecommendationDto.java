package com.silenteight.bridge.core.recommendation.domain.model;

import lombok.Builder;

import com.silenteight.bridge.core.registration.adapter.outgoing.jdbc.AlertWithoutMatches;
import com.silenteight.bridge.core.registration.adapter.outgoing.jdbc.MatchWithAlertId;
import com.silenteight.bridge.core.registration.domain.model.BatchIdWithPolicy;

import java.time.OffsetDateTime;
import java.util.List;

public record RecommendationDto(BatchIdWithPolicy batchId,
                                AlertWithoutMatches alert,
                                List<MatchWithAlertId> matchWithAlertIds,
                                RecommendationWithMetadata recommendation,
                                OffsetDateTime recommendedAtForErrorAlerts,
                                BatchStatistics statistics) {

  @Builder
  public RecommendationDto {}
}
