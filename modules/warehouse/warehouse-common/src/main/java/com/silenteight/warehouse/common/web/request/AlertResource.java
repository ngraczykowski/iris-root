package com.silenteight.warehouse.common.web.request;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

import static java.lang.String.format;
import static java.util.UUID.fromString;
import static org.apache.commons.lang3.StringUtils.removeStart;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AlertResource {

  private static final String RESOURCE_NAME_PREFIX = "alerts/";

  public static UUID fromResourceName(String resourceName) {
    return fromString(removeStart(resourceName, RESOURCE_NAME_PREFIX));
  }

  public static String toResourceName(UUID id) {
    return format("%s%s", RESOURCE_NAME_PREFIX, id);
  }
}
