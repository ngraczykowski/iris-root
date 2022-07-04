/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.domain.exceptons;

import java.io.Serial;

public class IngestJsonMessageException extends RuntimeException {

  @Serial
  private static final long serialVersionUID = -4440044017775261252L;

  public IngestJsonMessageException() {
  }
}
