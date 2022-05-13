package com.silenteight.hsbc.bridge.retention;

import lombok.Builder;
import lombok.Value;

import java.time.Duration;

@Value
@Builder
class DataRetentionJobProperties {

  Duration dataRetentionDuration;
  DataRetentionType type;
  int chunkSize;
}
