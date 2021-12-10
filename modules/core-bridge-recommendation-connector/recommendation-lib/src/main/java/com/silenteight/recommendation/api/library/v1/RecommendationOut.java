package com.silenteight.recommendation.api.library.v1;

import lombok.Builder;
import lombok.Value;

import com.silenteight.proto.recommendation.api.v1.Recommendation;

import java.time.OffsetDateTime;

@Value
@Builder
public class RecommendationOut {

  String alert;
  String name;
  String recommendedAction;
  String recommendationComment;
  OffsetDateTime recommendedAt;

  static RecommendationOut createFrom(Recommendation recommendation) {
    return RecommendationOut.builder()
        .alert(recommendation.getAlert())
        .name(recommendation.getName())
        .recommendedAction(recommendation.getRecommendedAction())
        .recommendationComment(recommendation.getRecommendationComment())
        .recommendedAt(TimestampUtil.toOffsetDateTime(recommendation.getRecommendedAtOrBuilder()))
        .build();
  }
}
