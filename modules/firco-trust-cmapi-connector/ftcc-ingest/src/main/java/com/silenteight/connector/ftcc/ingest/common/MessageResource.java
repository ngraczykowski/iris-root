package com.silenteight.connector.ftcc.ingest.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MessageResource {

  private static final String RESOURCE_NAME_PREFIX = "messages/";

  public static String toResourceName(UUID id) {
    return Resource.toResourceName(RESOURCE_NAME_PREFIX, id);
  }

  public static UUID fromResourceName(String resourceName) {
    return Resource.fromResourceName(RESOURCE_NAME_PREFIX, resourceName);
  }
}
