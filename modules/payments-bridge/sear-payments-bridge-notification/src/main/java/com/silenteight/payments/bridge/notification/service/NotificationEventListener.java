package com.silenteight.payments.bridge.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.notification.model.NotificationEvent;
import com.silenteight.payments.bridge.notification.port.NotificationAccessPort;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationEventListener {

  private final NotificationAccessPort notificationAccessPort;

  @EventListener
  public void handleNotificationEvent(NotificationEvent notificationEvent) {
    log.info("NotificationEvent has been received");
    notificationAccessPort.insert(notificationEvent.getNotification());
  }

}
