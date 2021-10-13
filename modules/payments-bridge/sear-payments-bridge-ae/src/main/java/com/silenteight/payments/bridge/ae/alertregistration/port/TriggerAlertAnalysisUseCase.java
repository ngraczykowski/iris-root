package com.silenteight.payments.bridge.ae.alertregistration.port;

import com.silenteight.payments.bridge.ae.alertregistration.domain.TriggerAlertRequest;

public interface TriggerAlertAnalysisUseCase {

  void triggerAlertAnalysis(TriggerAlertRequest triggerAlertRequest);
}
