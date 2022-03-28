package com.silenteight.payments.bridge.ae.alertregistration.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.ae.alertregistration.port.AnalysisDataAccessPort;
import com.silenteight.payments.bridge.ae.alertregistration.port.BuildCreateAnalysisRequestPort;
import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
class GetCurrentAnalysisUseCase {

  private final AnalysisDataAccessPort analysisDataAccessPort;
  private final CreateAnalysisService createAnalysisService;
  private final BuildCreateAnalysisRequestPort buildCreateAnalysisRequestPort;

  @Timed(percentiles = {0.5, 0.95, 0.99}, histogram = true)
  public String getOrCreateAnalysis() {
    return analysisDataAccessPort
        .findCurrentAnalysis()
        .orElseGet(this::createNewAnalysis);
  }

  private String createNewAnalysis() {
    return createAnalysisService.createAnalysis(
        buildCreateAnalysisRequestPort.buildFromCurrentModel());
  }
}
