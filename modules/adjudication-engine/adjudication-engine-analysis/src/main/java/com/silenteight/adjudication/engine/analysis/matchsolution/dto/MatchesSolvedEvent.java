package com.silenteight.adjudication.engine.analysis.matchsolution.dto;

import lombok.Value;

import com.silenteight.adjudication.internal.v1.MatchesSolved;

@Value
public class MatchesSolvedEvent {

  MatchesSolved matchesSolved;
}
