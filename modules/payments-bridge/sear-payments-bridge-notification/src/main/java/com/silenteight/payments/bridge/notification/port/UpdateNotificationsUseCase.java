package com.silenteight.payments.bridge.notification.port;

import com.silenteight.payments.bridge.notification.model.NotificationStatus;

import java.util.List;

public interface UpdateNotificationsUseCase {

  void update(List<Long> ids, NotificationStatus status);

}
