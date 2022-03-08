package com.silenteight.scb.ingest.adapter.incomming.common.gnsparty;

import lombok.Value;

@Value
class RecordInformation {

  int numberOfColumns;
  String sourceSystemIdentifier;
}