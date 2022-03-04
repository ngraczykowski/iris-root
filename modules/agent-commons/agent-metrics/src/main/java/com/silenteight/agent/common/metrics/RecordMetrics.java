
package com.silenteight.agent.common.metrics;

import java.util.List;

public interface RecordMetrics {

  String getName();

  List<MetricTag> getTags();

  void record(MetricsRecorder recorder);
}
