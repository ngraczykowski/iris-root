package com.silenteight.serp.governance.model.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.serp.governance.common.web.request.AbstractResourceExtractor;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ModelResource extends AbstractResourceExtractor {

  private static final String RESOURCE_NAME_PREFIX = "solvingModels/";

  public static String toResourceName(UUID id) {
    return RESOURCE_NAME_PREFIX + id.toString();
  }

  public static UUID fromResourceName(String resourceName) {
    return fromResourceName(resourceName, RESOURCE_NAME_PREFIX);
  }
}
