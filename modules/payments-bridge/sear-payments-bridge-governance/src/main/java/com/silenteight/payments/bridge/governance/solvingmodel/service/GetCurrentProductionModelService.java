package com.silenteight.payments.bridge.governance.solvingmodel.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.governance.solvingmodel.model.AnalysisModel;
import com.silenteight.payments.bridge.governance.solvingmodel.port.GetCurrentProductionModelUseCase;

import org.springframework.stereotype.Service;

import static com.silenteight.payments.bridge.governance.solvingmodel.service.AnalysisModelMapper.fromSolvingModel;

@Service
@RequiredArgsConstructor
@Slf4j
class GetCurrentProductionModelService implements GetCurrentProductionModelUseCase {

  private final SolvingModelServiceClient client;

  @Override
  public AnalysisModel getModel() {
    var currentModel = client.getCurrentModel();

    return fromSolvingModel(currentModel);
  }
}
