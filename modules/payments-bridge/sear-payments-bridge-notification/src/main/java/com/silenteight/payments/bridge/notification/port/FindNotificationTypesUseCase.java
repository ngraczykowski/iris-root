package com.silenteight.payments.bridge.notification.port;

import com.silenteight.payments.bridge.notification.model.NotificationType;

import java.util.List;

public interface FindNotificationTypesUseCase {

  List<NotificationType> findAllNotificationTypes();
}
