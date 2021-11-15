package com.silenteight.payments.bridge.warehouse.index.port;

import com.silenteight.payments.bridge.event.WarehouseIndexRequestedEvent;

public interface IndexAlertPort {

  void send(WarehouseIndexRequestedEvent event);

}
