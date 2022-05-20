package com.silenteight.adjudication.engine.app.logstash;

import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public final class LogMarkers {

  public static final Marker AUDIT = MarkerFactory.getDetachedMarker("AUDIT");

  private LogMarkers() {
  }
}
