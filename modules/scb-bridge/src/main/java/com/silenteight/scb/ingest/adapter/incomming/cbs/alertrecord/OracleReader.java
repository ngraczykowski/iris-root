package com.silenteight.scb.ingest.adapter.incomming.cbs.alertrecord;

public interface OracleReader {

  default int getMaxPageSize() {
    return 1_000;
  }
}
