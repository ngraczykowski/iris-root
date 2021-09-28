package com.silenteight.payments.bridge.firco.core.alertmessage.port;

import com.silenteight.payments.bridge.common.model.AlertMessageModel;

import com.fasterxml.jackson.databind.node.ObjectNode;

public interface CreateAlertMessageUseCase {

  void createAlertMessage(AlertMessageModel alertModel, ObjectNode originalMessage);
}
