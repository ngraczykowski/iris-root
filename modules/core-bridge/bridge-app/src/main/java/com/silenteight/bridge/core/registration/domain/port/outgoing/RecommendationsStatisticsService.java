package com.silenteight.bridge.core.registration.domain.port.outgoing;

import com.silenteight.bridge.core.registration.domain.model.RecommendationsStatistics;

public interface RecommendationsStatisticsService {

  RecommendationsStatistics createRecommendationsStatistics(String analysisName);
}
