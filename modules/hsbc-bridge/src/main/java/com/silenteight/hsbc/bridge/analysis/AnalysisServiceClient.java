package com.silenteight.hsbc.bridge.analysis;

import com.silenteight.hsbc.bridge.analysis.dto.*;

public interface AnalysisServiceClient {

  AnalysisDatasetDto addDataset(AddDatasetRequestDto request);
  CreateAnalysisResponseDto createAnalysis(CreateAnalysisRequestDto request);
  GetAnalysisResponseDto getAnalysis(String analysis);
}
