package com.silenteight.adjudication.engine.analysis.matchsolution.dto;

import lombok.Value;

import com.silenteight.solving.api.v1.SolutionResponse;

@Value
public class MatchSolution {

  long alertId;

  long matchId;

  SolutionResponse response;
}
