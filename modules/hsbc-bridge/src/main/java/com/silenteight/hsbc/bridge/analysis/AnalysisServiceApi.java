package com.silenteight.hsbc.bridge.analysis;

import com.silenteight.hsbc.bridge.analysis.dto.*;
import com.silenteight.hsbc.bridge.recommendation.RecommendationDto;

import java.util.List;

public interface AnalysisServiceApi {

  AnalysisDatasetDto addDataset(AddDatasetRequestDto request);
  CreateAnalysisResponseDto createAnalysis(CreateAnalysisRequestDto request);
  List<RecommendationDto> getRecommendations(GetRecommendationsDto request);
}
