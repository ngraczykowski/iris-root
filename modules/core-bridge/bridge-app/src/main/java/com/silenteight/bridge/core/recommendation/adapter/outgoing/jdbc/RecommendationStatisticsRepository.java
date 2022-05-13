package com.silenteight.bridge.core.recommendation.adapter.outgoing.jdbc;

import java.util.List;
import java.util.Map;

public interface RecommendationStatisticsRepository {

  Map<String, Long> getRecommendationStatistics(String analysisName);

  Map<String, Long> getRecommendationStatistics(String analysisName, List<String> alertsNames);
}
