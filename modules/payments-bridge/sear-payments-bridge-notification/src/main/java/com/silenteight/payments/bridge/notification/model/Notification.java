package com.silenteight.payments.bridge.notification.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Notification {

  private Long id;
  private String type;
  private String message;
  private byte[] attachment;
  private String attachmentName;
  private NotificationStatus status;
}
