package com.silenteight.payments.bridge.notification.model;

import lombok.RequiredArgsConstructor;
import lombok.Value;

@RequiredArgsConstructor
@Value
public class NotificationEvent {

  Notification notification;
}
