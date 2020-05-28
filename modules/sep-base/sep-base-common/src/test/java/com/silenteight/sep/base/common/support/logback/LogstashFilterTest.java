package com.silenteight.sep.base.common.support.logback;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.spi.FilterReply;
import org.assertj.core.api.AbstractComparableAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.annotation.Nonnull;

import static ch.qos.logback.classic.Level.DEBUG;
import static ch.qos.logback.classic.Level.ERROR;
import static ch.qos.logback.classic.Level.INFO;
import static ch.qos.logback.core.spi.FilterReply.DENY;
import static ch.qos.logback.core.spi.FilterReply.NEUTRAL;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LogstashFilterTest {

  private final LogstashFilter underTest = new LogstashFilter();

  @Test
  void testSerpLogging() {
    assertThatFiltering("com.silenteight.serp.LoggingClass", ERROR).isEqualTo(NEUTRAL);
    assertThatFiltering("com.silenteight.serp.LoggingClass", INFO).isEqualTo(NEUTRAL);
    assertThatFiltering("com.silenteight.serp.AnotherClass", DEBUG).isEqualTo(DENY);
  }

  @Test
  void testGenericLogging() {
    assertThatFiltering("com.example.NotSilentClass", INFO).isEqualTo(DENY);
    assertThatFiltering("com.example.NotSilentClass", ERROR).isEqualTo(NEUTRAL);
  }

  @Nonnull
  private AbstractComparableAssert<?, FilterReply> assertThatFiltering(
      String loggerName, Level eventLevel) {

    ILoggingEvent event = mockEvent(loggerName, eventLevel);
    return assertThat(underTest.decide(event));
  }

  private static ILoggingEvent mockEvent(String loggerName, Level eventLevel) {
    ILoggingEvent event = mock(ILoggingEvent.class);

    when(event.getLoggerName()).thenReturn(loggerName);
    when(event.getLevel()).thenReturn(eventLevel);

    return event;
  }
}
