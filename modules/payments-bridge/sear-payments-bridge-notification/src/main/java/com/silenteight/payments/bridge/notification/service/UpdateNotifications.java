package com.silenteight.payments.bridge.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.notification.model.NotificationStatus;
import com.silenteight.payments.bridge.notification.port.NotificationAccessPort;
import com.silenteight.payments.bridge.notification.port.UpdateNotificationsUseCase;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
class UpdateNotifications implements UpdateNotificationsUseCase {

  private final NotificationAccessPort notificationAccessPort;

  @Override
  public void update(List<Long> ids, NotificationStatus status) {
    notificationAccessPort.update(ids, status);
  }
}
