/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.gnsrt.recommendation;

public class InvalidGnsRtRequestDataException extends RuntimeException {

  private static final long serialVersionUID = 5116276160761223498L;

  public InvalidGnsRtRequestDataException(String message) {
    super(message);
  }
}
