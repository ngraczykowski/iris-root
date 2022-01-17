package com.silenteight.payments.bridge.svb.newlearning.job;

import com.silenteight.payments.bridge.agents.model.HistoricalRiskAssessmentAgentRequest;
import com.silenteight.payments.bridge.agents.model.HistoricalRiskAssessmentAgentResponse;
import com.silenteight.payments.bridge.agents.port.HistoricalRiskAssessmentUseCase;

public class HistoricalRiskAssessmentUseCaseMock implements HistoricalRiskAssessmentUseCase {

  @Override
  public HistoricalRiskAssessmentAgentResponse invoke(
      HistoricalRiskAssessmentAgentRequest historicalRiskAssessmentAgentRequest) {
    return HistoricalRiskAssessmentAgentResponse.NO;
  }
}
