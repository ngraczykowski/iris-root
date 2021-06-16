package com.silenteight.adjudication.engine.analysis.recommendation.domain;

import lombok.Value;

@Value
public class AlertSolution {

  long alertId;

  String recommendedAction;
}
