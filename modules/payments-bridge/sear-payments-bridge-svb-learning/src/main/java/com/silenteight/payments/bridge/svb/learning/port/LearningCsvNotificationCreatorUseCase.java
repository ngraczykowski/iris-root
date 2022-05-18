package com.silenteight.payments.bridge.svb.learning.port;

import com.silenteight.payments.bridge.notification.model.Notification;
import com.silenteight.payments.bridge.svb.learning.domain.LearningCsvNotificationRequest;

public interface LearningCsvNotificationCreatorUseCase {

  Notification createLearningCsvNotification(
      LearningCsvNotificationRequest learningCsvNotificationRequest);
}
