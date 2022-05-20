package com.silenteight.sep.base.common.messaging.metrics;

import lombok.Builder;
import lombok.NonNull;

import com.silenteight.sep.base.common.messaging.metrics.Size.SizeUnit;

import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.Arrays;

class SizeDistributionMetrics implements MeterBinder {

  private final String name;
  private final String description;
  private final SizeUnit unit;
  private final long[] slaBoundaries;
  private final Duration bucketExpiry;

  @Nullable
  private DistributionSummary summary;

  @Builder
  private SizeDistributionMetrics(
      @NonNull String name,
      @NonNull String description,
      @NonNull SizeUnit unit,
      @NonNull long[] slaBoundaries,
      @NonNull Duration bucketExpiry) {
    this.name = name;
    this.description = description;
    this.unit = unit;
    this.slaBoundaries = Arrays.copyOf(slaBoundaries, slaBoundaries.length);
    this.bucketExpiry = bucketExpiry;
  }

  private String getBaseUnit() {
    return String.format("%s (%s)", unit.getName(), unit.getAbbreviation());
  }

  void record(Size size) {
    if (summary == null)
      return;

    summary.record(size.convert(unit).getValue());
  }

  @Override
  public void bindTo(MeterRegistry registry) {
    summary = DistributionSummary
        .builder(name)
        .description(description)
        .baseUnit(getBaseUnit())
        .publishPercentileHistogram()
        .distributionStatisticExpiry(bucketExpiry)
        .sla(slaBoundaries)
        .register(registry);
  }
}
