package com.silenteight.payments.bridge.svb.newlearning.job;

import com.silenteight.datasource.api.historicaldecisions.v2.HistoricalDecisionsFeatureInput;
import com.silenteight.payments.bridge.agents.model.HistoricalRiskAssessmentAgentFeatureRequest;
import com.silenteight.payments.bridge.agents.port.HistoricalRiskAssessmentFeatureUseCase;

public class HistoricalRiskAssessmentFeatureUseCaseMock
    implements HistoricalRiskAssessmentFeatureUseCase {

  @Override
  public HistoricalDecisionsFeatureInput invoke(
      HistoricalRiskAssessmentAgentFeatureRequest historicalRiskAssessmentAgentRequest) {
    return HistoricalDecisionsFeatureInput.newBuilder().build();
  }
}
