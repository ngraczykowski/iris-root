package com.silenteight.payments.bridge.firco.datasource.port;

import com.silenteight.payments.bridge.firco.datasource.model.CmapiNotificationRequest;
import com.silenteight.payments.bridge.notification.model.Notification;

public interface CmapiNotificationCreatorUseCase {

  Notification createCmapiNotification(CmapiNotificationRequest cmapiNotificationRequest);

}
