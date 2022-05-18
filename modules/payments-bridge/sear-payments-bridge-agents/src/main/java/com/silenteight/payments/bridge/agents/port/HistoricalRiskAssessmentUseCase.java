package com.silenteight.payments.bridge.agents.port;

import com.silenteight.payments.bridge.agents.model.HistoricalRiskAssessmentAgentRequest;
import com.silenteight.payments.bridge.agents.model.HistoricalRiskAssessmentAgentResponse;

public interface HistoricalRiskAssessmentUseCase {

  HistoricalRiskAssessmentAgentResponse invoke(
      HistoricalRiskAssessmentAgentRequest historicalRiskAssessmentAgentRequest);
}
