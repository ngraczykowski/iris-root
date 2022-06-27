package com.silenteight.recommendation.api.library.v1;

import lombok.Builder;
import lombok.Value;

import com.silenteight.proto.recommendation.api.v1.RecommendationsRequest;

import java.util.List;

@Value
@Builder
public class RecommendationsIn {

  @Deprecated
  @Builder.Default
  String analysisName = "";
  @Builder.Default
  String batchId = "";

  @Builder.Default
  List<String> alertNames = List.of();

  RecommendationsRequest toRecommendationsRequest() {
    return RecommendationsRequest.newBuilder()
        .setBatchId(this.batchId)
        .setAnalysisName(analysisName)
        .addAllAlertNames(alertNames)
        .build();
  }
}
