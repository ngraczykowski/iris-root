package com.silenteight.payments.bridge.ae.alertregistration.port;

import com.silenteight.payments.bridge.ae.alertregistration.domain.AddAlertRequest;

public interface AddAlertsToAnalysisUseCase {

  void addAlerts(AddAlertRequest addAlertRequest);
}
