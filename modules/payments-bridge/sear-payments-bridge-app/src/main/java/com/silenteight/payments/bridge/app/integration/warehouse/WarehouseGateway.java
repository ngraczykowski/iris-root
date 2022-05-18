package com.silenteight.payments.bridge.app.integration.warehouse;


import com.silenteight.payments.bridge.warehouse.index.port.IndexAlertPort;

import org.springframework.integration.annotation.MessagingGateway;

import static com.silenteight.payments.bridge.app.integration.warehouse.WarehouseOutboundAmqpIntegrationConfiguration.WAREHOUSE_UPDATE_OUTBOUND;

@MessagingGateway(defaultRequestChannel = WAREHOUSE_UPDATE_OUTBOUND)
@SuppressWarnings("unused")
interface WarehouseGateway extends IndexAlertPort {}
