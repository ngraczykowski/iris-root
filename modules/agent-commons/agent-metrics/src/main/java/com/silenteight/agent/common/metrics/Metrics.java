package com.silenteight.agent.common.metrics;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class Metrics {

  public static void record(MetricsTemplate template) {
    record(template.build());
  }

  @RecordableMetrics
  public static void record(RecordMetrics metrics) {
    // dummy method
  }
}
