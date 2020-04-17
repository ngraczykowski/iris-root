package com.silenteight.sens.webapp.logging;

import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public final class SensWebappLogMarkers {

  public static final Marker USER_MANAGEMENT = MarkerFactory.getMarker("USER_MANAGEMENT");

  public static final Marker REASONING_BRANCH = MarkerFactory.getMarker("REASONING_BRANCH");

  public static final Marker KEYCLOAK_MIGRATION = MarkerFactory.getMarker("KEYCLOAK_MIGRATION");

  public static final Marker INTERNAL = MarkerFactory.getMarker("INTERNAL");

  public static final Marker CHANGE_REQUEST = MarkerFactory.getMarker("CHANGE_REQUEST");

  private SensWebappLogMarkers() {
  }
}
