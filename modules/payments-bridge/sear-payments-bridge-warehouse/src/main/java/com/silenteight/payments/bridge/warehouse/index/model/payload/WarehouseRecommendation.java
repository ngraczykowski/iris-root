package com.silenteight.payments.bridge.warehouse.index.model.payload;

import lombok.Builder;
import lombok.Value;

import com.fasterxml.jackson.databind.PropertyNamingStrategy.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@Value
@Builder
@JsonNaming(SnakeCaseStrategy.class)
public class WarehouseRecommendation implements IndexPayload {

  String fircoSystemId;
  String recommendationName;
  String createTime;  // from Recommendation.getCreateTime()
  String recommendedAction;  // from Recommendation.getAction()
  String recommendationComment;  // from Recommendation.getComment()
  String status;
  String deliveryStatus;
  String reason;
}
