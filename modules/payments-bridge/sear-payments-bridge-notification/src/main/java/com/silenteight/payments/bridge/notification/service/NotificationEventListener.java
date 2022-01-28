package com.silenteight.payments.bridge.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.common.environment.EnvironmentProperties;
import com.silenteight.payments.bridge.notification.model.NotificationEvent;
import com.silenteight.payments.bridge.notification.port.NotificationAccessPort;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@EnableConfigurationProperties(EnvironmentProperties.class)
class NotificationEventListener {

  private final NotificationAccessPort notificationAccessPort;
  private final EnvironmentProperties environmentProperties;

  @EventListener
  public void handleNotificationEvent(NotificationEvent notificationEvent) {
    log.info("NotificationEvent has been received");
    var notification = notificationEvent.getNotification();
    String subjectWithEnvironment =
        String.format("%s [%s]", notification.getSubject(), environmentProperties.getName());
    notification.setSubject(subjectWithEnvironment);
    notificationAccessPort.insert(notification);
  }
}
