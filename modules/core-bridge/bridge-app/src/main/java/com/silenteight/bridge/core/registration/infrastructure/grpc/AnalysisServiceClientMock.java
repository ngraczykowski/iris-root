package com.silenteight.bridge.core.registration.infrastructure.grpc;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.library.v1.analysis.*;

import java.util.List;

@Slf4j
public class AnalysisServiceClientMock implements AnalysisServiceClient {

  @Override
  public AnalysisDatasetOut addDataset(AddDatasetIn request) {
    log.info("MOCK: Add dataset");
    return null;
  }

  @Override
  public CreateAnalysisOut createAnalysis(CreateAnalysisIn request) {
    log.info("MOCK: Create analysis");
    return CreateAnalysisOut.builder()
        .name("analysis_name")
        .policy("analysis_policy")
        .strategy("analysis_strategy")
        .build();
  }

  @Override
  public GetAnalysisOut getAnalysis(String analysis) {
    log.info("MOCK: Get analysis");
    return null;
  }

  @Override
  public AddAlertsToAnalysisOut addAlertsToAnalysis(AddAlertsToAnalysisIn request) {
    log.info("MOCK: Add alerts to analysis: " + request);
    return AddAlertsToAnalysisOut.builder()
        .addedAlerts(List.of())
        .build();
  }
}
