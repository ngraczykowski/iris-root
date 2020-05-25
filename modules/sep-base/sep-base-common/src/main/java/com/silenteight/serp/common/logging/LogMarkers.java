package com.silenteight.serp.common.logging;

import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public final class LogMarkers {

  public static final Marker INFRASTRUCTURE = MarkerFactory.getDetachedMarker("INFRASTRUCTURE");

  public static final Marker INTERNAL = MarkerFactory.getDetachedMarker("INTERNAL");

  private LogMarkers() {
  }
}
