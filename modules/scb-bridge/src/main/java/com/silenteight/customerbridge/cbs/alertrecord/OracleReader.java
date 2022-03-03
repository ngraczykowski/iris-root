package com.silenteight.customerbridge.cbs.alertrecord;

public interface OracleReader {

  default int getMaxPageSize() {
    return 1_000;
  }
}
