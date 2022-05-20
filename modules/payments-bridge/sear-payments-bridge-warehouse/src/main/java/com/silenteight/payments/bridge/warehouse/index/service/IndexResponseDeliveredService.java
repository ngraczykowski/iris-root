package com.silenteight.payments.bridge.warehouse.index.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.warehouse.index.model.IndexResponseDeliveredRequest;
import com.silenteight.payments.bridge.warehouse.index.model.RequestOrigin;
import com.silenteight.payments.bridge.warehouse.index.model.payload.WarehouseResponseDelivery;
import com.silenteight.payments.bridge.warehouse.index.port.IndexResponseDeliveredUseCase;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class IndexResponseDeliveredService implements IndexResponseDeliveredUseCase {

  private final IndexedAlertBuilderFactory alertBuilderFactory;
  private final IndexAlertService indexService;

  public void index(IndexResponseDeliveredRequest request) {
    var alert = alertBuilderFactory
        .newBuilder()
        .setDiscriminator(request.getDiscriminator())
        .addPayload(WarehouseResponseDelivery.builder()
            .status(request.getStatus())
            .deliveryStatus(
                request.getDeliveryStatus()
            )
            .build())
        .build();
    indexService.index(alert, RequestOrigin.UNSET);
  }

}
