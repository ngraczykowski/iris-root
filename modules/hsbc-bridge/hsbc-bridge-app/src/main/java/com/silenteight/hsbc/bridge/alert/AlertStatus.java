package com.silenteight.hsbc.bridge.alert;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum AlertStatus {

  COMPLETED(false),
  ERROR(false),
  LEARNING_COMPLETED(true),
  LEARNING_REGISTERED_IN_DB(true),
  STORED(false),
  PRE_PROCESSED(true),
  PROCESSING(false),
  RECOMMENDATION_READY(true);

  @Getter
  private boolean internal;
}
