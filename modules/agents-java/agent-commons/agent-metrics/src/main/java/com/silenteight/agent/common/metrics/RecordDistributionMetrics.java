package com.silenteight.agent.common.metrics;

import lombok.*;

import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@Value
@EqualsAndHashCode(doNotUseGetters = true)
@Builder
public class RecordDistributionMetrics implements RecordMetrics {

  @NonNull
  String name;
  @Singular
  @NonNull
  List<MetricTag> tags;
  @Nullable
  BucketBoundaries bucketBoundaries;
  int value;

  public Optional<BucketBoundaries> getBucketBoundaries() {
    return ofNullable(bucketBoundaries);
  }

  @Override
  public void record(MetricsRecorder recorder) {
    recorder.record(this);
  }
}
