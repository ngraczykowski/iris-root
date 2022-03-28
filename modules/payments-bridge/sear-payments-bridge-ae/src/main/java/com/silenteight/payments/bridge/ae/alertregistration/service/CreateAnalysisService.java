package com.silenteight.payments.bridge.ae.alertregistration.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.CreateAnalysisRequest;
import com.silenteight.payments.bridge.ae.alertregistration.port.AnalysisClientPort;
import com.silenteight.payments.bridge.ae.alertregistration.port.AnalysisDataAccessPort;
import com.silenteight.payments.bridge.ae.alertregistration.port.CreateAnalysisUseCase;
import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class CreateAnalysisService implements CreateAnalysisUseCase {

  private final AnalysisClientPort analysisClient;
  private final AnalysisDataAccessPort analysisDataAccessPort;

  @Override
  @Timed(percentiles = {0.5, 0.95, 0.99}, histogram = true)
  public String createAnalysis(CreateAnalysisRequest request) {
    var analysis = analysisClient.createAnalysis(request);
    analysisDataAccessPort.save(analysis.getName());
    return analysis.getName();
  }
}
