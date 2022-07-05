/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.payments.bridge.firco.callback.port;

import com.silenteight.payments.bridge.firco.alertmessage.model.AlertMessageStatus;

import java.util.UUID;

public interface AlertUndeliveredPort {

  void sendUndelivered(UUID alertId);

  void sendUndelivered(UUID alertId, AlertMessageStatus status);

}
