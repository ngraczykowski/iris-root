package com.silenteight.adjudication.engine.analysis.matchsolution.dto;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import com.silenteight.solving.api.v1.SolutionResponse;

import java.util.List;

@Value
@Builder
public class MatchSolution {

  long alertId;

  long matchId;

  String clientMatchIdentifier;

  SolutionResponse response;

  @Singular
  List<Category> categories;

  @Singular
  List<Feature> features;
}
