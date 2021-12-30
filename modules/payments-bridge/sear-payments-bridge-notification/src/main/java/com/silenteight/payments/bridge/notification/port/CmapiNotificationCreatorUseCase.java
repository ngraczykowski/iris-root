package com.silenteight.payments.bridge.notification.port;

import com.silenteight.payments.bridge.notification.model.CmapiNotificationRequest;
import com.silenteight.payments.bridge.notification.model.Notification;

import java.util.List;

public interface CmapiNotificationCreatorUseCase {

  Notification createCmapiNotification(CmapiNotificationRequest cmapiNotificationRequest);

  byte[] mergeCsvAttachments(List<byte[]> attachments);

}
