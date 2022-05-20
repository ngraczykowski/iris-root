package com.silenteight.adjudication.engine.analysis.matchsolution.dto;

import lombok.Value;

import java.util.List;

@Value
public class MatchSolutionCollection {

  long analysisId;

  List<MatchSolution> matchSolutions;
}
