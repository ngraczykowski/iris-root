package com.silenteight.agent.common.metrics.micrometer;

import com.silenteight.agent.common.metrics.*;

import io.micrometer.core.instrument.distribution.HistogramSnapshot;
import io.micrometer.core.instrument.distribution.HistogramSupport;
import io.micrometer.core.instrument.search.Search;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.time.Duration.ofSeconds;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.*;

class MicrometerRecorderTest {

  private static final String AGENT_PREFIX = "agent.prefix";

  private MicrometerRecorder recorder;
  private SimpleMeterRegistry registry;

  @BeforeEach
  void setUp() {
    registry = new SimpleMeterRegistry();
    recorder = new MicrometerRecorder(registry, AGENT_PREFIX);
  }

  @Test
  void shouldRecordCounterMetrics() {
    var metricName = "counter";
    var tag1 = new MetricTag("tag", "tag1Value");
    var tag2 = new MetricTag("tag", "tag2Value");

    recorder.record(new RecordCounterMetrics(metricName, tag1));
    recorder.record(new RecordCounterMetrics(metricName, tag2));
    recorder.record(new RecordCounterMetrics(metricName, List.of(tag2), 3));

    assertThat(findMetric(metricName))
        .extracting(search -> search.tags(tag1.getName(), tag1.getValue()).counter())
        .satisfies(counter -> assertThat(counter.count()).isEqualTo(1L));
    assertThat(findMetric(metricName))
        .extracting(search -> search.tags(tag2.getName(), tag2.getValue()).counter())
        .satisfies(counter -> assertThat(counter.count()).isEqualTo(4L));
  }

  @Test
  void shouldRecordTimerMetrics() {
    var metricName = "timer";
    var tag1 = new MetricTag("tag", "tag1Value");
    var tag2 = new MetricTag("tag", "tag2Value");

    recorder.record(new RecordTimerMetrics(metricName, ofSeconds(1L), tag1));
    recorder.record(new RecordTimerMetrics(metricName, ofSeconds(2L), tag2));
    recorder.record(new RecordTimerMetrics(metricName, ofSeconds(3L), tag2));

    assertThat(findMetric(metricName))
        .extracting(search -> search.tags(tag1.getName(), tag1.getValue()).timer())
        .satisfies(timer -> assertThat(timer.count()).isEqualTo(1L))
        .satisfies(timer -> assertThat(timer.totalTime(SECONDS)).isEqualTo(1L));
    assertThat(findMetric(metricName))
        .extracting(search -> search.tags(tag2.getName(), tag2.getValue()).timer())
        .satisfies(timer -> assertThat(timer.count()).isEqualTo(2L))
        .satisfies(timer -> assertThat(timer.totalTime(SECONDS)).isEqualTo(5L));
  }

  @Test
  void shouldRecordDistributionMetrics() {
    var metricName = "distribution";
    var tag1 = new MetricTag("tag", "tag1Value");
    var tag2 = new MetricTag("tag", "tag2Value");
    var boundaries = BucketBoundaries.of(0, 5, 10);

    recorder.record(RecordDistributionMetrics.builder()
        .name(metricName).tag(tag1).bucketBoundaries(boundaries).value(5)
        .build());
    recorder.record(RecordDistributionMetrics.builder()
        .name(metricName).tag(tag2).bucketBoundaries(boundaries).value(10)
        .build());
    recorder.record(RecordDistributionMetrics.builder()
        .name(metricName).tag(tag2).bucketBoundaries(boundaries).value(0)
        .build());

    assertThat(findMetric(metricName))
        .extracting(search -> search.tags(tag1.getName(), tag1.getValue()).summary())
        .satisfies(summary -> assertThat(summary.totalAmount()).isEqualTo(5));
    assertThat(findMetric(metricName))
        .extracting(search -> search.tags(tag2.getName(), tag2.getValue()).summary())
        .satisfies(summary -> assertThat(summary.totalAmount()).isEqualTo(10));
  }

  @Test
  void shouldCreateBucketForZero() {
    var metricName = "distribution";
    var boundaries = BucketBoundaries.of(0);

    recorder.record(RecordDistributionMetrics.builder()
        .name(metricName).bucketBoundaries(boundaries).value(0)
        .build());

    assertThat(findMetric(metricName))
        .extracting(Search::summary)
        .extracting(HistogramSupport::takeSnapshot)
        .extracting(HistogramSnapshot::histogramCounts)
        .satisfies(counts -> assertThat(counts)
            .hasSize(1)
            .anySatisfy(bucket -> assertThat(bucket.bucket()).isLessThan(1.0))
            .anySatisfy(bucket -> assertThat(bucket.count()).isEqualTo(1)));
  }

  private Search findMetric(String metricName) {
    return registry.find(AGENT_PREFIX + "." + metricName);
  }
}
