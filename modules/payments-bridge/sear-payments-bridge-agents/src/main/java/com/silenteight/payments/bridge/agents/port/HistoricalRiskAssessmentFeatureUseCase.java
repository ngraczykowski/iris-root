package com.silenteight.payments.bridge.agents.port;

import com.silenteight.datasource.api.historicaldecisions.v2.HistoricalDecisionsFeatureInput;
import com.silenteight.payments.bridge.agents.model.HistoricalRiskAssessmentAgentFeatureRequest;

//NOTE(jgajewski): Remove 'Feature' from interface name 'HistoricalRiskAssessmentFeatureUseCase',
// when category use case is going to be deleted, after successful testing
public interface HistoricalRiskAssessmentFeatureUseCase {

  HistoricalDecisionsFeatureInput invoke(
      HistoricalRiskAssessmentAgentFeatureRequest historicalRiskAssessmentAgentRequest);
}
