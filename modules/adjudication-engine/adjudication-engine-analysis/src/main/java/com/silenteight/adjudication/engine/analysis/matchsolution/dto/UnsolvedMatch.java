package com.silenteight.adjudication.engine.analysis.matchsolution.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Value;

import com.silenteight.solving.api.v1.FeatureVector;
import com.silenteight.solving.api.v1.SolutionResponse;

import java.util.Arrays;

@Value
@Getter(AccessLevel.NONE)
public class UnsolvedMatch {

  long alertId;

  @Getter
  long matchId;

  String[] categoryValues;

  String[] featureValues;

  public FeatureVector toFeatureVector() {
    return FeatureVector.newBuilder()
        .addAllFeatureValue(Arrays.asList(categoryValues))
        .addAllFeatureValue(Arrays.asList(featureValues))
        .build();
  }

  public int getFeatureValueCount() {
    return categoryValues.length + featureValues.length;
  }

  public MatchSolution toMatchSolution(SolutionResponse response) {
    return new MatchSolution(alertId, matchId, response);
  }

  public String getMatchName() {
    return "alerts/" + alertId + "/matches/" + matchId;
  }
}
