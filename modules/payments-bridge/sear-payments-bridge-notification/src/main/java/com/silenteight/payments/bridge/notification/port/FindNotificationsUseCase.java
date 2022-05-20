package com.silenteight.payments.bridge.notification.port;

import com.silenteight.payments.bridge.notification.model.Notification;
import com.silenteight.payments.bridge.notification.model.NotificationStatus;

import java.util.List;

public interface FindNotificationsUseCase {

  List<Notification> findNotificationsByTypeAndStatus(String type, NotificationStatus status);
}
