package com.silenteight.hsbc.bridge.recommendation;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class StoreRecommendationCommand {

  String recommendedAction;
  String recommendationComment;
}
