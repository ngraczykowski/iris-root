package com.silenteight.payments.bridge.warehouse.index.model.payload;

import lombok.Builder;
import lombok.Value;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@Value
@Builder
@JsonNaming(SnakeCaseStrategy.class)
public class WarehouseMatch implements IndexPayload {

  String matchId;
  String matchingText;
}
