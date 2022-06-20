/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.alertrecord;

public interface OracleReader {

  default int getMaxPageSize() {
    return 1_000;
  }
}
