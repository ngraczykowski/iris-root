package com.silenteight.adjudication.engine.analysis.matchsolution.dto;

import lombok.Builder;
import lombok.Value;

import com.fasterxml.jackson.databind.node.ObjectNode;

@Value
@Builder
public class FeatureContext {

  String solution;

  ObjectNode reason;

  String agentConfig;
}
