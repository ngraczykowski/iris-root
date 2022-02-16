package com.silenteight.payments.bridge.warehouse.index.port;

import com.silenteight.payments.bridge.warehouse.index.model.IndexResponseDeliveredRequest;

public interface IndexResponseDeliveredUseCase {

  void index(IndexResponseDeliveredRequest request);
}
