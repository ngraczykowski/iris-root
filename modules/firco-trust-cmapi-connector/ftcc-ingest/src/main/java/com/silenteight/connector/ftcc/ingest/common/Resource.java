package com.silenteight.connector.ftcc.ingest.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

import static java.util.UUID.fromString;
import static org.apache.commons.lang3.StringUtils.removeStart;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class Resource {

  public static String toResourceName(String prefix, UUID id) {
    return prefix + id.toString();
  }

  public static UUID fromResourceName(String prefix, String resourceName) {
    return fromString(removeStart(resourceName, prefix));
  }
}
