package com.silenteight.adjudication.engine.analysis.analysis.integration;

import com.silenteight.adjudication.internal.v1.AddedAnalysisAlerts;

interface AddedAnalysisAlertsGateway {

  void send(AddedAnalysisAlerts addedAnalysisAlerts);
}
