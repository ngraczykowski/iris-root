package com.silenteight.payments.bridge.svb.learning.reader.port;

import com.silenteight.payments.bridge.common.resource.csv.file.provider.model.FileRequest;

public interface HandleLearningAlertsUseCase {

  void readAlerts(FileRequest learningRequest);
}
