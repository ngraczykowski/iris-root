package com.silenteight.payments.bridge.ae.alertregistration.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.ae.alertregistration.port.AnalysisClientPort;
import com.silenteight.payments.bridge.ae.alertregistration.port.AnalysisDataAccessPort;
import com.silenteight.payments.bridge.ae.alertregistration.port.CreateAnalysisUseCase;
import com.silenteight.payments.bridge.ae.alertregistration.port.ReceiveAnalysisModelUseCase;
import com.silenteight.payments.bridge.common.resource.ResourceName;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class CreateAnalysisService implements CreateAnalysisUseCase {

  private final ReceiveAnalysisModelUseCase receiveAnalysisModel;
  private final AnalysisClientPort analysisClient;
  private final AnalysisDataAccessPort analysisDataAccessPort;

  @Override
  public long createAnalysis() {
    var analysis = analysisClient.createAnalysis(receiveAnalysisModel.createAnalysisRequest());
    var analysisId = ResourceName.create(analysis.getName()).getLong("analysis");
    analysisDataAccessPort.save(analysisId);
    return analysisId;
  }
}
