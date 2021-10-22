package com.silenteight.payments.bridge.warehouse.index.model;

import com.silenteight.payments.bridge.event.WarehouseIndexRequestedEvent.IndexRequestOrigin;

public enum RequestOrigin {
  UNSET,
  LEARNING,
  CMAPI;

  public IndexRequestOrigin mapToIndexRequestOrigin() {
    return IndexRequestOrigin.valueOf(name());
  }
}
