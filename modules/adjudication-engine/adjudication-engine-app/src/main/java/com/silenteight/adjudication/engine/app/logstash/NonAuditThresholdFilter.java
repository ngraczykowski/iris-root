package com.silenteight.adjudication.engine.app.logstash;

import ch.qos.logback.classic.filter.ThresholdFilter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.spi.FilterReply;

import static ch.qos.logback.core.spi.FilterReply.DENY;
import static com.silenteight.adjudication.engine.app.logstash.LogMarkers.AUDIT;

/**
 * Deny AUDIT events from Cloud API, while all others pass on selected threshold.
 * <p/>
 * Used together with Logstash appender, to output there only events that carry business value.
 */
public class NonAuditThresholdFilter extends ThresholdFilter {

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
