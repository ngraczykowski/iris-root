package com.silenteight.simulator.processing.alert.index.grpc;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.adjudication.api.v1.Recommendation;

import com.google.protobuf.Timestamp;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class RecommendationFixtures {

  static final String NAME = "recommendations/d17b4708-6fde-8dc0-4832-d17b4708d8ca";
  static final String ALERT = "alerts/a9b45451-6fde-4832-8dc0-d17b4708d8ca";
  static final Timestamp CREATE_TIME = Timestamp.newBuilder()
      .setSeconds(1622505601)
      .setNanos(0)
      .build();
  static final String RECOMMENDED_ACTION = "FALSE_POSITIVE";
  static final String COMMENT = "This is not that person";

  static final Recommendation RECOMMENDATION =
      Recommendation.newBuilder()
          .setName(NAME)
          .setAlert(ALERT)
          .setCreateTime(CREATE_TIME)
          .setRecommendedAction(RECOMMENDED_ACTION)
          .setRecommendationComment(COMMENT)
          .build();
}
