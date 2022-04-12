package com.silenteight.adjudication.engine.solving.domain;

import lombok.Builder;

@Builder
public class AlertSolvingModel {

  public boolean isEmpty() {
    // TODO refactor when data
    return true;
  }

  public static final AlertSolvingModel empty() {
    return AlertSolvingModel.builder().build();
  }
}
