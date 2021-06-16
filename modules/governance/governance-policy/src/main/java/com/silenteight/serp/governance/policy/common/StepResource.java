package com.silenteight.serp.governance.policy.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.serp.governance.common.web.request.AbstractResourceExtractor;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
public final class StepResource extends AbstractResourceExtractor {

  private static final String RESOURCE_NAME_PREFIX = "steps/";

  public static UUID fromResourceName(String resourceName) {
    return fromResourceName(resourceName, RESOURCE_NAME_PREFIX);
  }
}
