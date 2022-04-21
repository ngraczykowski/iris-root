package com.silenteight.adjudication.engine.solving.application.process.dto;

import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor
public class MatchSolutionResponse {

  long alertId;

  long matchId;

  String solution;

  String reason;
}
