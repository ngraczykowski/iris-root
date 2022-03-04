package com.silenteight.agent.common.metrics;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.util.List;

@Value
@RequiredArgsConstructor
public class RecordCounterMetrics implements RecordMetrics  {

  private static final double DEFAULT_COUNTER_AMOUNT = 1d;

  @NonNull
  String name;
  @NonNull
  List<MetricTag> tags;
  double amount;

  public RecordCounterMetrics(String name, @NonNull MetricTag... tags) {
    this(name, List.of(tags));
  }

  public RecordCounterMetrics(String name, List<MetricTag> tags) {
    this(name, tags, DEFAULT_COUNTER_AMOUNT);
  }

  @Override
  public void record(MetricsRecorder recorder) {
    recorder.record(this);
  }
}
