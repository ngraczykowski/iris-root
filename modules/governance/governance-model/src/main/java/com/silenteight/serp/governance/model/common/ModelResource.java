package com.silenteight.serp.governance.model.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

import static java.util.UUID.fromString;
import static org.apache.commons.lang3.StringUtils.removeStart;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ModelResource {

  private static final String RESOURCE_NAME_PREFIX = "solvingModels/";

  public static String toResourceName(UUID id) {
    return RESOURCE_NAME_PREFIX + id.toString();
  }

  public static UUID fromResourceName(String resourceName) {
    return fromString(removeStart(resourceName, RESOURCE_NAME_PREFIX));
  }
}
