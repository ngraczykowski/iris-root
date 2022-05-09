package com.silenteight.sep.usermanagement.keycloak.query;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
class AttributeFilter {

  @NonNull
  private final List<Attribute> filter;

  boolean isConfigured() {
    return !filter.isEmpty();
  }

  boolean visible(Map<String, List<String>> userAttributes) {
    return filter
        .stream()
        .anyMatch(entry -> hasAttribute(userAttributes, entry.getName(), entry.getValue()));
  }

  private boolean hasAttribute(
      Map<String, List<String>> userAttributes, String name, String value) {
    return ofNullable(userAttributes)
        .orElse(emptyMap())
        .getOrDefault(name, emptyList())
        .contains(value);
  }
}
