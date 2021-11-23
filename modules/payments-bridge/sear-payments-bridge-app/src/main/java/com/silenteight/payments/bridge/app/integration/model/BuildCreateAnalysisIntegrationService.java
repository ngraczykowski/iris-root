package com.silenteight.payments.bridge.app.integration.model;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.CreateAnalysisRequest;
import com.silenteight.payments.bridge.ae.alertregistration.port.BuildCreateAnalysisRequestPort;
import com.silenteight.payments.bridge.governance.solvingmodel.port.GetCurrentProductionModelUseCase;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class BuildCreateAnalysisIntegrationService implements BuildCreateAnalysisRequestPort {

  private final GetCurrentProductionModelUseCase getCurrentProductionModelUseCase;
  private final CreateAnalysisRequestMapper createAnalysisRequestMapper;

  public CreateAnalysisRequest buildFromCurrentModel() {
    var model = getCurrentProductionModelUseCase.getModel();
    return createAnalysisRequestMapper.map(model);
  }

}
