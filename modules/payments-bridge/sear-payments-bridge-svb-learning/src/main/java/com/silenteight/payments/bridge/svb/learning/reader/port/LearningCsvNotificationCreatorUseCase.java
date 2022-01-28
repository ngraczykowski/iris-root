package com.silenteight.payments.bridge.svb.learning.reader.port;

import com.silenteight.payments.bridge.notification.model.Notification;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningCsvNotificationRequest;

public interface LearningCsvNotificationCreatorUseCase {

  Notification createLearningCsvNotification(
      LearningCsvNotificationRequest learningCsvNotificationRequest);
}
