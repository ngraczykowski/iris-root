package com.silenteight.hsbc.bridge.grpc;

import com.silenteight.hsbc.bridge.analysis.AnalysisServiceClient;
import com.silenteight.hsbc.bridge.analysis.dto.*;

import java.time.OffsetDateTime;
import java.util.Random;

class AnalysisServiceClientMock implements AnalysisServiceClient {

  private final Random random = new Random();

  @Override
  public AnalysisDatasetDto addDataset(AddDatasetRequestDto request) {
    return AnalysisDatasetDto.builder()
        .alertsCount(1 + random.nextInt(5))
        .name("dataset-" + random.nextInt(1000))
        .build();
  }

  @Override
  public CreateAnalysisResponseDto createAnalysis(CreateAnalysisRequestDto request) {
    return CreateAnalysisResponseDto.builder()
        .strategy("strategy")
        .policy("policy_" + OffsetDateTime.now())
        .name("analysis/" + random.nextInt(1000))
        .build();
  }

  @Override
  public GetAnalysisResponseDto getAnalysis(String analysis) {
    return GetAnalysisResponseDto.builder()
        .alertsCount(2)
        .pendingAlerts(random.nextInt(2))
        .build();
  }

}
