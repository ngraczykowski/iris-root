package com.silenteight.serp.common.support.logback;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;
import org.slf4j.Marker;

import static ch.qos.logback.classic.Level.ERROR;
import static ch.qos.logback.classic.Level.INFO;
import static ch.qos.logback.core.spi.FilterReply.DENY;
import static ch.qos.logback.core.spi.FilterReply.NEUTRAL;
import static com.silenteight.serp.common.logging.LogMarkers.INFRASTRUCTURE;
import static com.silenteight.serp.common.logging.LogMarkers.INTERNAL;

/**
 * Filters logging events from SERP on INFO level, while all others on ERROR level.
 * <p/>
 * Used together with Logstash appender, to output there only events that carry business value.
 */
public class LogstashFilter extends Filter<ILoggingEvent> {

  @Override
  public FilterReply decide(ILoggingEvent event) {
    Level threshold = event.getLoggerName().startsWith("com.silenteight.serp") ? INFO : ERROR;

    Marker marker = event.getMarker();
    if (marker != null && (marker.contains(INFRASTRUCTURE) || marker.contains(INTERNAL)))
      return DENY;

    if (event.getLevel().isGreaterOrEqual(threshold))
      return NEUTRAL;
    else
      return DENY;
  }
}
