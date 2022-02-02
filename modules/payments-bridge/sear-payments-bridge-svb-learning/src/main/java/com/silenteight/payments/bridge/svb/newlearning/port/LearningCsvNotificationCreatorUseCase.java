package com.silenteight.payments.bridge.svb.newlearning.port;

import com.silenteight.payments.bridge.notification.model.Notification;
import com.silenteight.payments.bridge.svb.newlearning.domain.LearningCsvNotificationRequest;

public interface LearningCsvNotificationCreatorUseCase {

  Notification createLearningCsvNotification(
      LearningCsvNotificationRequest learningCsvNotificationRequest);
}
