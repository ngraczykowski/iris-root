package com.silenteight.serp.governance.qa.manage.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.serp.governance.common.web.request.AbstractResourceExtractor;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
public final class AlertResource extends AbstractResourceExtractor {

  private static final String RESOURCE_NAME_PREFIX = "alerts/";

  public static UUID fromResourceName(String resourceName) {
    return fromResourceName(resourceName, RESOURCE_NAME_PREFIX);
  }

  public static String toResourceName(String id) {
    return toResourceName(id, RESOURCE_NAME_PREFIX);
  }
}
