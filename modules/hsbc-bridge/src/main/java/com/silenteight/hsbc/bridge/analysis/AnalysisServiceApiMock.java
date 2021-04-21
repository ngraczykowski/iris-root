package com.silenteight.hsbc.bridge.analysis;

import com.silenteight.hsbc.bridge.analysis.dto.*;
import com.silenteight.hsbc.bridge.recommendation.RecommendationDto;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Random;

class AnalysisServiceApiMock implements AnalysisServiceApi {

  @Override
  public AnalysisDatasetDto addDataset(AddDatasetRequestDto request) {
    return AnalysisDatasetDto.builder()
        .name("dataset-" + new Random().nextInt())
        .build();
  }

  @Override
  public AnalysisDto createAnalysis(CreateAnalysisRequestDto request) {
    return AnalysisDto.builder()
        .alertCount(1)
        .pendingAlerts(1)
        .strategy("strategy")
        .policy("policy_" + OffsetDateTime.now())
        .name("analysis/" + new Random().nextInt(1000))
        .features(List.of(
            FeatureDto.builder()
                .name("nationalityId")
                .agentConfig("/agents/document/config/1")
                .build()
        ))
        .build();
  }

  @Override
  public List<RecommendationDto> getRecommendations(GetRecommendationsDto request) {
    return List.of();
  }
}
