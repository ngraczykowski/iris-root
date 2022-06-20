/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.gnsparty;

import lombok.Value;

@Value
class RecordInformation {

  int numberOfColumns;
  String sourceSystemIdentifier;
}
