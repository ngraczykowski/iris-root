package com.silenteight.payments.bridge.common.model;

import lombok.Builder;
import lombok.Value;

import com.fasterxml.jackson.databind.PropertyNamingStrategy.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@Value
@Builder
@JsonNaming(SnakeCaseStrategy.class)
public class WarehouseRecommendation {

  String policy;  // from RecommendationMetadata ?
  String policyTitle;  // from RecommendationMetadata ?
  String createTime;  // from Recommendation.getCreateTime()
  String recommendedAction;  // from Recommendation.getAction()
  String recommendationComment;  // from Recommendation.getComment()
  String accessPermissionTag;
}
