package com.silenteight.adjudication.engine.analysis.matchsolution.dto;

import lombok.*;

import com.silenteight.solving.api.v1.FeatureVector;
import com.silenteight.solving.api.v1.SolutionResponse;

import java.util.List;

import static java.util.stream.Collectors.toUnmodifiableList;

@Value
@Builder
@Getter(AccessLevel.NONE)
public class UnsolvedMatch {

  long alertId;

  @Getter
  long matchId;

  String clientMatchIdentifier;

  @Singular
  List<Category> categories;

  @Singular
  List<Feature> features;

  public FeatureVector toFeatureVector() {
    return FeatureVector.newBuilder()
        .addAllFeatureValue(
            categories.stream().map(Category::getValue).collect(toUnmodifiableList()))
        .addAllFeatureValue(features.stream().map(Feature::getValue).collect(toUnmodifiableList()))
        .build();
  }

  public int getFeatureValueCount() {
    return categories.size() + features.size();
  }

  public MatchSolution toMatchSolution(SolutionResponse response) {
    return MatchSolution.builder()
        .alertId(alertId)
        .matchId(matchId)
        .clientMatchIdentifier(clientMatchIdentifier)
        .response(response)
        .categories(categories)
        .features(features)
        .build();
  }

  public String getMatchName() {
    return "alerts/" + alertId + "/matches/" + matchId;
  }
}
