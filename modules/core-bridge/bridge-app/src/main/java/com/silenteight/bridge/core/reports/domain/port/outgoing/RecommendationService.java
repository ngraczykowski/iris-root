package com.silenteight.bridge.core.reports.domain.port.outgoing;

import com.silenteight.bridge.core.reports.domain.model.RecommendationWithMetadataDto;

import java.util.List;

public interface RecommendationService {

  List<RecommendationWithMetadataDto> getRecommendations(String analysisName);
}
