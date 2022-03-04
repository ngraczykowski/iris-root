package com.silenteight.agent.common.metrics.micrometer;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import com.silenteight.agent.common.metrics.*;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.binder.MeterBinder;
import io.micrometer.core.lang.NonNull;

import static com.silenteight.agent.common.metrics.micrometer.TagsMapper.map;
import static java.lang.String.format;


@AllArgsConstructor
@NoArgsConstructor
public class MicrometerRecorder implements MeterBinder, MetricsRecorder {

  private static final double[] PERCENTILES = new double[] { 0.5, 0.95 };

  private MeterRegistry registry;
  private String metricPrefix;

  @Override
  public void bindTo(@NonNull MeterRegistry registry) {
    this.registry = registry;
  }

  @Override
  public void record(@NonNull RecordCounterMetrics command) {
    if (registry == null) {
      return;
    }

    handleCounterMetrics(command);
  }

  @Override
  public void record(@NonNull RecordTimerMetrics command) {
    if (registry == null) {
      return;
    }

    handleTimerMetrics(command);
  }

  @Override
  public void record(@NonNull RecordDistributionMetrics command) {
    if (registry == null) {
      return;
    }

    handleDistributionMetrics(command);
  }

  private void handleCounterMetrics(RecordCounterMetrics event) {
    Counter counter = Counter
        .builder(formatName(event))
        .tags(map(event.getTags()))
        .register(registry);

    counter.increment(event.getAmount());
  }

  private void handleDistributionMetrics(RecordDistributionMetrics event) {
    var builder = DistributionSummary
        .builder(formatName(event))
        .tags(map(event.getTags()));

    event.getBucketBoundaries()
        .map(ServiceLevelObjectivesMapper::map)
        .ifPresent(builder::serviceLevelObjectives);

    DistributionSummary summary = builder
        .publishPercentiles(PERCENTILES)
        .register(registry);

    summary.record(event.getValue());
  }

  private void handleTimerMetrics(RecordTimerMetrics event) {
    Timer timer = Timer
        .builder(formatName(event))
        .tags(map(event.getTags()))
        .publishPercentiles(PERCENTILES)
        .register(registry);

    timer.record(event.getDuration());
  }

  private String formatName(RecordMetrics event) {
    return format("%s.%s", metricPrefix, event.getName());
  }
}
