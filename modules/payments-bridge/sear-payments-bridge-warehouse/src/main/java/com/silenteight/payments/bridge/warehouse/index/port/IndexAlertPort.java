package com.silenteight.payments.bridge.warehouse.index.port;

import com.silenteight.payments.bridge.warehouse.index.model.WarehouseIndexRequestedEvent;

public interface IndexAlertPort {

  void send(WarehouseIndexRequestedEvent event);

}
