package com.silenteight.hsbc.bridge.analysis;

import com.silenteight.hsbc.bridge.analysis.dto.*;

import java.util.List;

public interface AnalysisServiceApi {

  AnalysisDatasetDto addDataset(AddDatasetRequestDto request);
  AnalysisDto createAnalysis(CreateAnalysisRequestDto request);
  List<RecommendationDto> getRecommendations(GetRecommendationsDto request);
}
