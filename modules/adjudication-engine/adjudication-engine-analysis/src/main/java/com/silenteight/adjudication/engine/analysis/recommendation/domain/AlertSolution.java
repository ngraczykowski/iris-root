package com.silenteight.adjudication.engine.analysis.recommendation.domain;

import lombok.Builder;
import lombok.Value;

import com.fasterxml.jackson.databind.node.ObjectNode;

@Value
@Builder
public class AlertSolution {

  long alertId;

  String recommendedAction;

  long[] matchIds;

  ObjectNode[] matchContexts;
}
