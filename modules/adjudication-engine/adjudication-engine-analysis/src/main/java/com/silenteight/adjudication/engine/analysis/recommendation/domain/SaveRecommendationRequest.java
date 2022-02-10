package com.silenteight.adjudication.engine.analysis.recommendation.domain;

import lombok.Value;

import java.util.List;

@Value
public class SaveRecommendationRequest {

  long analysisId;

  boolean shouldRecommendationAttach;

  List<AlertSolution> alertSolutions;
}
