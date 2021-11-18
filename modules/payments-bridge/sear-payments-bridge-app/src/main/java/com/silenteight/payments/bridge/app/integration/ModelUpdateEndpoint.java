package com.silenteight.payments.bridge.app.integration;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.ae.alertregistration.port.CreateAnalysisUseCase;
import com.silenteight.payments.bridge.app.integration.model.CreateAnalysisRequestMapper;
import com.silenteight.payments.bridge.event.ModelUpdatedEvent;
import com.silenteight.payments.bridge.governance.solvingmodel.model.AnalysisModel;

import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;

@RequiredArgsConstructor
@MessageEndpoint
class ModelUpdateEndpoint {

  private final CreateAnalysisUseCase createAnalysisUseCase;
  private final CreateAnalysisRequestMapper createAnalysisRequestMapper;

  @ServiceActivator(inputChannel = ModelUpdatedEvent.CHANNEL)
  void analysisModelUpdated(ModelUpdatedEvent event) {
    var model = event.getData(AnalysisModel.class);
    var request = createAnalysisRequestMapper.map(model);
    createAnalysisUseCase.createAnalysis(request);
  }

}
