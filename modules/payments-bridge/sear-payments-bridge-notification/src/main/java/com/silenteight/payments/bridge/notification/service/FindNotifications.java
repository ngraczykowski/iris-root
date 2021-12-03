package com.silenteight.payments.bridge.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.notification.model.Notification;
import com.silenteight.payments.bridge.notification.model.NotificationStatus;
import com.silenteight.payments.bridge.notification.port.FindNotificationsUseCase;
import com.silenteight.payments.bridge.notification.port.NotificationAccessPort;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
class FindNotifications implements FindNotificationsUseCase {

  private final NotificationAccessPort notificationAccessPort;

  @Override
  public List<Notification> findNotificationsByTypeAndStatus(
      String type, NotificationStatus status) {
    return notificationAccessPort.findNotificationsByTypeAndStatus(type, status);
  }
}
