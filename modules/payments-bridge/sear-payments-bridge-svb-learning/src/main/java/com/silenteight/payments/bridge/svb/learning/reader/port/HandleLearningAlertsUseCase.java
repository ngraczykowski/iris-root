package com.silenteight.payments.bridge.svb.learning.reader.port;

import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningRequest;

public interface HandleLearningAlertsUseCase {

  void readAlerts(LearningRequest learningRequest);
}
