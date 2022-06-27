package com.silenteight.simulator.dataset.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

import static java.util.UUID.fromString;
import static java.util.stream.Collectors.toSet;
import static org.apache.commons.lang3.StringUtils.removeStart;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DatasetResource {

  private static final String RESOURCE_NAME_PREFIX = "datasets/";

  public static String toResourceName(UUID id) {
    return RESOURCE_NAME_PREFIX + id.toString();
  }

  public static UUID fromResourceName(String resourceName) {
    return fromString(removeStart(resourceName, RESOURCE_NAME_PREFIX));
  }

  public static Set<UUID> fromResourceNamesSet(Set<String> datasets) {
    return datasets.stream().map(DatasetResource::fromResourceName).collect(toSet());
  }
}
