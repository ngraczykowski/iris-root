package com.silenteight.payments.bridge.warehouse.index.model.payload;

import lombok.Builder;
import lombok.Value;

import com.fasterxml.jackson.databind.PropertyNamingStrategy.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@Value
@Builder
@JsonNaming(SnakeCaseStrategy.class)
public class WarehouseResponseDelivery implements IndexPayload {

  String status;
  String deliveryStatus;

}
