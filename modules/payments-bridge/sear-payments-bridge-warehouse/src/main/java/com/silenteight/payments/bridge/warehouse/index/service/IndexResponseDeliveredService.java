package com.silenteight.payments.bridge.warehouse.index.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.common.model.AlertData;
import com.silenteight.payments.bridge.warehouse.index.model.RequestOrigin;
import com.silenteight.payments.bridge.warehouse.index.model.payload.WarehouseResponseDelivery;
import com.silenteight.payments.bridge.warehouse.index.port.IndexResponseDeliveredUseCase;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class IndexResponseDeliveredService implements IndexResponseDeliveredUseCase {

  private final IndexedAlertBuilderFactory alertBuilderFactory;
  private final IndexAlertService indexService;

  public void index(AlertData alertData, String status, String deliveryStatus) {
    var alert = alertBuilderFactory
        .newBuilder()
        .setDiscriminator(alertData.getDiscriminator())
        .addPayload(WarehouseResponseDelivery.builder()
            .status(status)
            .deliveryStatus(deliveryStatus)
            .build())
        .build();
    indexService.index(alert, RequestOrigin.UNSET);
  }

}
