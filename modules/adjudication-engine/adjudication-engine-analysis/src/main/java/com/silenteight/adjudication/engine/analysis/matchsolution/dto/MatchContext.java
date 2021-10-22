package com.silenteight.adjudication.engine.analysis.matchsolution.dto;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Map;

@Value
@Builder
public class MatchContext {

  String matchId;

  String solution;

  ObjectNode reason;

  @Singular
  Map<String, String> categories;

  @Singular
  Map<String, FeatureContext> features;
}
