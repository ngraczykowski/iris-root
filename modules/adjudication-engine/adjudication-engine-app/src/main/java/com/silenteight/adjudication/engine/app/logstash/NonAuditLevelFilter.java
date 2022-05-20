package com.silenteight.adjudication.engine.app.logstash;

import ch.qos.logback.classic.filter.LevelFilter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.spi.FilterReply;

import static ch.qos.logback.core.spi.FilterReply.DENY;
import static com.silenteight.adjudication.engine.app.logstash.LogMarkers.AUDIT;

/**
 * Deny AUDIT events, while all others pass on selected level.
 * <p/>
 * Used together with Logstash appender, to output there only events that carry business value.
 */
public class NonAuditLevelFilter extends LevelFilter {

  @Override
  public FilterReply decide(ILoggingEvent event) {
    var marker = event.getMarker();
    if (marker != null && marker.contains(AUDIT)) {
      return DENY;
    } else {
      return super.decide(event);
    }
  }
}
