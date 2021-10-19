package com.silenteight.payments.bridge.warehouse.index.port;

import com.silenteight.data.api.v1.Alert;
import com.silenteight.payments.bridge.warehouse.index.model.IndexRequestId;

import java.util.stream.Stream;

public interface IndexAlertUseCase {

  IndexRequestId index(Iterable<Alert> alerts);

  default IndexRequestId index(Alert alert) {
    return index(() -> Stream.of(alert).iterator());
  }
}
