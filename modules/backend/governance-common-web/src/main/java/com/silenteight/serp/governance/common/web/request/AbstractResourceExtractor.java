package com.silenteight.serp.governance.common.web.request;

import java.util.UUID;

import static java.util.UUID.fromString;
import static org.apache.commons.lang3.StringUtils.removeStart;

public abstract class AbstractResourceExtractor {

  protected static UUID fromResourceName(String resourceName, String prefix) {
    return fromString(removeStart(resourceName, prefix));
  }

  public static String toResourceName(UUID id, String prefix) {
    return toResourceName(id.toString(), prefix);
  }

  public static String toResourceName(String id, String prefix) {
    return prefix + id;
  }
}
