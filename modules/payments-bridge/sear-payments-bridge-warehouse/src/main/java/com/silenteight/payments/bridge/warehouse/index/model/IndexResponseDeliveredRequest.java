package com.silenteight.payments.bridge.warehouse.index.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class IndexResponseDeliveredRequest {

  String discriminator;

  String deliveryStatus;

  String status;
}
