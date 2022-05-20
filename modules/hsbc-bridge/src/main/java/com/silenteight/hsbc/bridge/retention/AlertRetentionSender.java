package com.silenteight.hsbc.bridge.retention;

import java.time.OffsetDateTime;

public interface AlertRetentionSender {

  void send(OffsetDateTime expireDate, int chunkSize, DataRetentionType type);
}
