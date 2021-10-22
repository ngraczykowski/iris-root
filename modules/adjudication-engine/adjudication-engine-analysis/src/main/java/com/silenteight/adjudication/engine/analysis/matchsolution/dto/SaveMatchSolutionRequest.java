package com.silenteight.adjudication.engine.analysis.matchsolution.dto;

import lombok.Builder;
import lombok.Value;

import com.fasterxml.jackson.databind.node.ObjectNode;

@Value
@Builder
public class SaveMatchSolutionRequest {

  long analysisId;

  long matchId;

  String solution;

  ObjectNode reason;

  ObjectNode context;

  public String getReasonString() {
    return reason.toString();
  }

  public String getContextString() {
    return context.toString();
  }
}
