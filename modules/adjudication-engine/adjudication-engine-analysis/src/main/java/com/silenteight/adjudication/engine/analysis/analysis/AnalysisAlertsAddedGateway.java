package com.silenteight.adjudication.engine.analysis.analysis;

import com.silenteight.adjudication.internal.v1.AnalysisAlertsAdded;

public interface AnalysisAlertsAddedGateway {

  void send(AnalysisAlertsAdded addedAnalysisAlerts);
}
