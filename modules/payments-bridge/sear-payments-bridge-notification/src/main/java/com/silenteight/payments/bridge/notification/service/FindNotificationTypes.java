package com.silenteight.payments.bridge.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.notification.model.NotificationType;
import com.silenteight.payments.bridge.notification.port.FindNotificationTypesUseCase;
import com.silenteight.payments.bridge.notification.port.NotificationTypeAccessPort;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
class FindNotificationTypes implements FindNotificationTypesUseCase {

  private final NotificationTypeAccessPort notificationTypeAccessPort;

  @Override
  public List<NotificationType> findAllNotificationTypes() {
    return notificationTypeAccessPort.findAllNotificationTypes();
  }
}
