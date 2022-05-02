package com.silenteight.recommendation.api.library.v1;

import lombok.Builder;
import lombok.Value;

import com.silenteight.proto.recommendation.api.v1.Match;

import java.util.Map;

@Value
@Builder
public class MatchOut {

  String id;
  String recommendedAction;
  String recommendationComment;
  String stepId;
  String fvSignature;
  Map<String, String> features;
  String name;

  static MatchOut createFrom(Match match) {
    return MatchOut.builder()
        .id(match.getId())
        .recommendedAction(match.getRecommendedAction())
        .recommendationComment(match.getRecommendationComment())
        .stepId(match.getStepId())
        .fvSignature(match.getFvSignature())
        .features(StructMapperUtil.toMap(match.getFeatures()))
        .name(match.getName())
        .build();
  }
}
