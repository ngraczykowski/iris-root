package com.silenteight.payments.bridge.warehouse.index.service;

import com.silenteight.payments.bridge.event.WarehouseIndexRequestedEvent;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

import static com.silenteight.payments.bridge.common.integration.CommonChannels.WAREHOUSE_UPDATE_OUTBOUND;

@MessagingGateway
interface IndexAlertPublisher {

  @Gateway(requestChannel = WAREHOUSE_UPDATE_OUTBOUND)
  void send(WarehouseIndexRequestedEvent event);

}
