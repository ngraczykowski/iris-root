package com.silenteight.bridge.core.recommendation.domain.port.outgoing;

import com.silenteight.bridge.core.recommendation.domain.model.BatchWithAlertsDto;

public interface RegistrationService {

  BatchWithAlertsDto getBatchWithAlerts(String analysisName);
}
