package com.silenteight.payments.bridge.app.integration;

import lombok.RequiredArgsConstructor;

import com.silenteight.model.api.v1.SolvingModel;
import com.silenteight.payments.bridge.ae.alertregistration.port.CreateAnalysisUseCase;
import com.silenteight.payments.bridge.app.integration.model.CreateAnalysisRequestMapper;
import com.silenteight.payments.bridge.governance.solvingmodel.model.AnalysisModel;
import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class ModelUpdateIntegrationService {

  private final CreateAnalysisUseCase createAnalysisUseCase;
  private final CreateAnalysisRequestMapper createAnalysisRequestMapper;

  @Timed(histogram = true, percentiles = { 0.5, 0.95, 0.99 })
  void analysisModelUpdated(SolvingModel model) {
    var analysisModel = AnalysisModel.fromSolvingModel(model);
    var request = createAnalysisRequestMapper.map(analysisModel);
    createAnalysisUseCase.createAnalysis(request);
  }

}
