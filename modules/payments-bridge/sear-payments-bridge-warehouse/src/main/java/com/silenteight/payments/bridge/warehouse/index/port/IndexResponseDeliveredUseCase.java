package com.silenteight.payments.bridge.warehouse.index.port;

import com.silenteight.payments.bridge.common.model.AlertData;

public interface IndexResponseDeliveredUseCase {

  void index(AlertData alert, String status, String deliveryStatus);
}
