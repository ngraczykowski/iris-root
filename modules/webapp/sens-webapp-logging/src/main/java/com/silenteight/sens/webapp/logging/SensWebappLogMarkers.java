package com.silenteight.sens.webapp.logging;

import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public final class SensWebappLogMarkers {

  public static final Marker USER_MANAGEMENT = MarkerFactory.getMarker("USER_MANAGEMENT");
  public static final Marker INTERNAL = MarkerFactory.getMarker("INTERNAL");

  private SensWebappLogMarkers() {
  }
}
