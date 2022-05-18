package com.silenteight.payments.bridge.notification.service;

import com.silenteight.payments.bridge.notification.model.Notification;
import com.silenteight.payments.bridge.notification.model.NotificationEvent;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;

import java.util.EventObject;

import static org.mockito.Mockito.*;

@SpringBootTest(classes = NotificationEventListenerTest.class)
class NotificationEventListenerTest {

  @Autowired
  private ApplicationEventPublisher applicationEventPublisher;

  @MockBean
  private NotificationEventListener notificationEventListener;

  @Test
  void handleNotificationEvent_publishNotificationEvents_methodIsInvokedTwoTimes() {

    var notificationEvent = new NotificationEvent(Notification.builder().build());

    applicationEventPublisher.publishEvent(notificationEvent);
    applicationEventPublisher.publishEvent(notificationEvent);

    verify(notificationEventListener, times(2)).handleNotificationEvent(notificationEvent);
  }

  @Test
  void handleNotificationEvent_publishSomeEvent_methodIsNotInvoked() {

    var someEvent = new EventObject(Notification.builder().build());

    applicationEventPublisher.publishEvent(someEvent);

    verify(notificationEventListener, times(0)).handleNotificationEvent(any());
  }
}
