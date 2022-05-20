package com.silenteight.payments.bridge.notification.adapter.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.notification.model.NotificationType;
import com.silenteight.payments.bridge.notification.port.NotificationTypeAccessPort;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
class NotificationTypeDataAccess implements NotificationTypeAccessPort {

  private final FindNotificationType findNotificationType;

  @Override
  public List<NotificationType> findAllNotificationTypes() {
    return findNotificationType.findAllNotificationTypes();
  }
}
