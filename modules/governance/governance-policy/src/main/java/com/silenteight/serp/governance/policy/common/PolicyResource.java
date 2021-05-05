package com.silenteight.serp.governance.policy.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.serp.governance.common.web.request.AbstractResourceExtractor;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
public final class PolicyResource extends AbstractResourceExtractor {

  private static final String RESOURCE_NAME_PREFIX = "policies/";

  public static UUID fromResourceName(String resourceName) {
    return fromResourceName(resourceName, RESOURCE_NAME_PREFIX);
  }
}
