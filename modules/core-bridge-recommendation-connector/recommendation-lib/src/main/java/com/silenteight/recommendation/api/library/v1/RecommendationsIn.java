package com.silenteight.recommendation.api.library.v1;

import lombok.Builder;
import lombok.Value;

import com.silenteight.proto.recommendation.api.v1.RecommendationsRequest;

import java.util.List;

@Value
@Builder
public class RecommendationsIn {

  String analysisName;

  @Builder.Default
  List<String> alertNames = List.of();

  RecommendationsRequest toRecommendationsRequest() {
    return RecommendationsRequest.newBuilder()
        .setAnalysisName(this.analysisName)
        .addAllAlertNames(alertNames)
        .build();
  }
}
