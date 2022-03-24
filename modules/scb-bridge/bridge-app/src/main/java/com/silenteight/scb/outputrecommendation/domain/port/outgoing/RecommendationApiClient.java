package com.silenteight.scb.outputrecommendation.domain.port.outgoing;

import com.silenteight.scb.outputrecommendation.domain.model.Recommendations;

import java.util.List;

public interface RecommendationApiClient {

  Recommendations getRecommendations(String analysisName, List<String> alertNames);
}
