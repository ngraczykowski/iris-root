package com.silenteight.payments.bridge.warehouse.index.model.payload;

import lombok.Builder;
import lombok.Getter;
import lombok.Value;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.Map;

@Value
@Builder
@JsonNaming(SnakeCaseStrategy.class)
public class WarehouseMatchRecommendation implements IndexPayload {

  String solution;
  Map<String, String> categoryValues;
  Map<String, String> featureValues;

  @Getter(onMethod_ = @JsonAnyGetter)
  Map<String, JsonNode> additionalData;
}
