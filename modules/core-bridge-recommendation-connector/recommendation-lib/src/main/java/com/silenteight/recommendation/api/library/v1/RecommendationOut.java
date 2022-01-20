package com.silenteight.recommendation.api.library.v1;

import lombok.Builder;
import lombok.Value;

import com.silenteight.proto.recommendation.api.v1.Recommendation;

import java.time.OffsetDateTime;
import java.util.List;

@Value
@Builder
public class RecommendationOut {

  String batchId;
  String policyId;
  String name;
  String recommendedAction;
  String recommendationComment;
  OffsetDateTime recommendedAt;
  AlertOut alert;
  List<MatchOut> matches;

  static RecommendationOut createFrom(Recommendation recommendation) {
    return RecommendationOut.builder()
        .batchId(recommendation.getBatchId())
        .policyId(recommendation.getPolicyId())
        .name(recommendation.getName())
        .recommendedAction(recommendation.getRecommendedAction())
        .recommendationComment(recommendation.getRecommendationComment())
        .recommendedAt(TimestampUtil.toOffsetDateTime(recommendation.getRecommendedAtOrBuilder()))
        .alert(AlertOut.createFrom(recommendation.getAlert()))
        .matches(recommendation.getMatchesList().stream()
            .map(MatchOut::createFrom)
            .toList())
        .build();
  }
}
