package com.silenteight.adjudication.engine.analysis.recommendation.domain;

import lombok.Builder;
import lombok.Value;

import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Map;

@Value
@Builder
public class AlertSolution {

  long alertId;

  String recommendedAction;

  long[] matchIds;

  ObjectNode[] matchContexts;

  String comment;

  Map<String, String> matchComments;

  ObjectNode alertLabels;
}
