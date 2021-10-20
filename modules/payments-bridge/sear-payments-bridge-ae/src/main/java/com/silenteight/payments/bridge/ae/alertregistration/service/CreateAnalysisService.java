package com.silenteight.payments.bridge.ae.alertregistration.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.CreateAnalysisRequest;
import com.silenteight.payments.bridge.ae.alertregistration.port.AnalysisClientPort;
import com.silenteight.payments.bridge.ae.alertregistration.port.AnalysisDataAccessPort;
import com.silenteight.payments.bridge.ae.alertregistration.port.CreateAnalysisUseCase;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class CreateAnalysisService implements CreateAnalysisUseCase {

  private final AnalysisClientPort analysisClient;
  private final AnalysisDataAccessPort analysisDataAccessPort;

  @Override
  public String createAnalysis(CreateAnalysisRequest request) {
    var analysis = analysisClient.createAnalysis(request);
    analysisDataAccessPort.save(analysis.getName());
    return analysis.getName();
  }
}
