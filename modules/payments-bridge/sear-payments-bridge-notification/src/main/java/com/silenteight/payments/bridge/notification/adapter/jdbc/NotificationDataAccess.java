package com.silenteight.payments.bridge.notification.adapter.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.notification.model.Notification;
import com.silenteight.payments.bridge.notification.model.NotificationStatus;
import com.silenteight.payments.bridge.notification.port.NotificationAccessPort;

import org.springframework.stereotype.Component;

import java.util.List;


@Component
@RequiredArgsConstructor
class NotificationDataAccess implements NotificationAccessPort {

  private final InsertNotificationRequest insertNotificationRequest;
  private final FindNotification findNotification;
  private final UpdateNotification updateNotification;


  @Override
  public void insert(Notification notification) {
    insertNotificationRequest.insert(notification);
  }

  @Override
  public List<Notification> findNotificationsByTypeAndStatus(
      String type, NotificationStatus status) {
    return findNotification.findNotificationsByTypeAndStatus(type, status);
  }

  @Override
  public void update(List<Long> ids, NotificationStatus status) {
    updateNotification.update(ids, status);
  }
}
