package com.silenteight.adjudication.engine.app.logstash;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

import static ch.qos.logback.core.spi.FilterReply.DENY;
import static ch.qos.logback.core.spi.FilterReply.NEUTRAL;
import static com.silenteight.adjudication.engine.app.logstash.LogMarkers.AUDIT;

/**
 * Pass AUDIT events, while dany all others.
 * <p/>
 * Used together with Logstash appender, to output there only events that carry business value.
 */
public class AuditPassingFilter extends Filter<ILoggingEvent> {

  @Override
  public FilterReply decide(ILoggingEvent event) {
    var marker = event.getMarker();
    if (marker != null && marker.contains(AUDIT)) {
      return NEUTRAL;
    } else {
      return DENY;
    }
  }
}
