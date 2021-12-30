package com.silenteight.payments.bridge.notification.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CmapiNotificationRequest {

  String alertId;
  String alertName;
  String messageId;
  String systemId;
  String message;
}
