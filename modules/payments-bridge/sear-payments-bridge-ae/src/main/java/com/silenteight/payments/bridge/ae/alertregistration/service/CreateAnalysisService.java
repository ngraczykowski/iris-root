package com.silenteight.payments.bridge.ae.alertregistration.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.ae.alertregistration.port.AnalysisClientPort;
import com.silenteight.payments.bridge.ae.alertregistration.port.AnalysisDataAccessPort;
import com.silenteight.payments.bridge.ae.alertregistration.port.CreateAnalysisUseCase;
import com.silenteight.payments.bridge.ae.alertregistration.port.ReceiveAnalysisModelUseCase;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class CreateAnalysisService implements CreateAnalysisUseCase {

  private final ReceiveAnalysisModelUseCase receiveAnalysisModel;
  private final AnalysisClientPort analysisClient;
  private final AnalysisDataAccessPort analysisDataAccessPort;

  @Override
  public String createAnalysis() {
    var analysis = analysisClient.createAnalysis(receiveAnalysisModel.createAnalysisRequest());
    analysisDataAccessPort.save(analysis.getName());
    return analysis.getName();
  }
}
