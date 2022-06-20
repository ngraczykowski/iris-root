/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.gateway;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class CbsOutput {

  private static final String SUCCESS_CODE = "000";

  private String statusCode;
  @NonNull
  private State state = State.OK;

  public boolean isCbsErrorStatus() {
    return !SUCCESS_CODE.equals(statusCode);
  }

  public enum State {
    OK,
    TEMPORARY_FAILURE,
    ERROR,
  }
}
