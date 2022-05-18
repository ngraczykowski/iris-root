package com.silenteight.payments.bridge.warehouse.index.port;

import com.silenteight.payments.bridge.warehouse.index.model.IndexAlertRegisteredRequest;

public interface IndexAlertRegisteredUseCase {

  void index(IndexAlertRegisteredRequest request);

}
