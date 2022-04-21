package com.silenteight.simulator.management.timeout;

import com.silenteight.sep.base.common.time.TimeSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.Instant;

import static java.time.Duration.ofSeconds;
import static java.time.Instant.now;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SimulationLastCheckTimesTest {

  private static final String SCENARIO_NAME = "test";

  @Mock
  private TimeSource timeSource;

  private final Duration interval = ofSeconds(1);

  private SimulationLastCheckTimes underTest;

  @BeforeEach
  void setup() {
    underTest = new SimulationLastCheckTimes(timeSource, interval);
  }

  @Test
  void checkOnNewScenarioShouldBeDelayed() {
    Instant now = now();
    when(timeSource.now()).thenReturn(now);

    boolean result = underTest.isIntervalElapsed(SCENARIO_NAME);

    assertFalse(result);
  }

  @Test
  void checkShouldBePerformedAfterInterval() {
    Instant now = now();
    when(timeSource.now()).thenReturn(now);

    underTest.isIntervalElapsed(SCENARIO_NAME);

    when(timeSource.now()).thenReturn(now.plusSeconds(interval.toSeconds() + 1));

    boolean result = underTest.isIntervalElapsed(SCENARIO_NAME);
    assertTrue(result);
  }

  @Test
  void lastCheckTimestampShouldBeUpdated() {
    Instant now = now();
    when(timeSource.now()).thenReturn(now);

    underTest.isIntervalElapsed(SCENARIO_NAME);

    Instant afterInterval = now.plusSeconds(interval.toSeconds() + 1);
    when(timeSource.now()).thenReturn(afterInterval);

    boolean result = underTest.isIntervalElapsed(SCENARIO_NAME);
    assertTrue(result);

    underTest.updateSimulationCheckTimestamp(SCENARIO_NAME);

    result = underTest.isIntervalElapsed(SCENARIO_NAME);
    assertFalse(result);
  }
}
