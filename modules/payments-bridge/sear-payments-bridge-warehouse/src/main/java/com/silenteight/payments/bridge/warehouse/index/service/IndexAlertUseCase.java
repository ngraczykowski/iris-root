package com.silenteight.payments.bridge.warehouse.index.service;

import com.silenteight.data.api.v2.Alert;
import com.silenteight.payments.bridge.warehouse.index.model.IndexRequestId;
import com.silenteight.payments.bridge.warehouse.index.model.RequestOrigin;

import java.util.stream.Stream;

public interface IndexAlertUseCase {

  IndexRequestId index(Iterable<Alert> alerts, RequestOrigin origin);

  default IndexRequestId index(Alert alert, RequestOrigin origin) {
    return index(() -> Stream.of(alert).iterator(), origin);
  }
}
