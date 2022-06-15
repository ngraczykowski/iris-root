/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.data.jdbc;

class NoMatchFeatureFound extends RuntimeException {
  public NoMatchFeatureFound(String message) {
    super(message);
  }
}
