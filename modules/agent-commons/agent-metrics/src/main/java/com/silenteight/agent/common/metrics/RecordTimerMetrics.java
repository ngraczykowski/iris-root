package com.silenteight.agent.common.metrics;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.time.Duration;
import java.util.List;

@Value
@RequiredArgsConstructor
public class RecordTimerMetrics implements RecordMetrics {

  @NonNull
  String name;
  @NonNull
  List<MetricTag> tags;
  @NonNull
  Duration duration;

  public RecordTimerMetrics(String name, Duration duration, @NonNull MetricTag... tags) {
    this(name, List.of(tags), duration);
  }

  @Override
  public void record(MetricsRecorder recorder) {
    recorder.record(this);
  }
}
