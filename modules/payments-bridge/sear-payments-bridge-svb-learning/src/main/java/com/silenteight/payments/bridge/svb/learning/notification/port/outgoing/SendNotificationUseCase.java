package com.silenteight.payments.bridge.svb.learning.notification.port.outgoing;

import com.silenteight.payments.bridge.svb.learning.notification.model.NotificationRequest;

public interface SendNotificationUseCase {

  void sendNotification(NotificationRequest request);
}
