package com.silenteight.agent.common.metrics;

import lombok.NonNull;

public interface MetricsRecorder {

  void record(@NonNull RecordCounterMetrics command);

  void record(@NonNull RecordDistributionMetrics command);

  void record(@NonNull RecordTimerMetrics command);
}
