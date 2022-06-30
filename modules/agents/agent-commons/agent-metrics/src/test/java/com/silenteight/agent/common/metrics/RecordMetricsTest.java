package com.silenteight.agent.common.metrics;

import org.aspectj.lang.Aspects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecordMetricsTest {

  @Spy
  private MetricsRecorder recorder;

  @BeforeEach
  void givenRecorderSpy() {
    MetricsAspect aspect = Aspects.aspectOf(MetricsAspect.class);
    aspect.setRecorder(recorder);
    aspect.setMetricsEnabled(true);
  }

  @Test
  void shouldRecordCounterMetrics() {
    var metrics = new RecordCounterMetrics("name", new MetricTag("tag", "value"));

    Metrics.record(() -> metrics);

    var captor = forClass(RecordCounterMetrics.class);
    verify(recorder).record(captor.capture());
    assertThat(captor.getValue()).isEqualTo(metrics);
  }

  @Test
  void shouldRecordTimerMetrics() {
    var metrics =
        new RecordTimerMetrics("name", Duration.ofSeconds(1L), new MetricTag("tag", "value"));

    Metrics.record(() -> metrics);

    var captor = forClass(RecordTimerMetrics.class);
    verify(recorder).record(captor.capture());
    assertThat(captor.getValue()).isEqualTo(metrics);
  }

  @Test
  void shouldRecordDistributionMetrics() {
    var metrics = RecordDistributionMetrics.builder().name("name").build();

    Metrics.record(() -> metrics);

    var captor = forClass(RecordDistributionMetrics.class);
    verify(recorder).record(captor.capture());
    assertThat(captor.getValue()).isEqualTo(metrics);
  }
}
