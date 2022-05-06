package com.silenteight.sep.auth.authorization;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.validation.constraints.NotNull;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toSet;

@Validated
@ConfigurationProperties(prefix = "sep.auth")
@RequiredArgsConstructor
@Data
public class AuthorizationProperties {

  static final String DEFAULT_PRINCIPAL_CLAIM = "preferred_username";

  static final String[] DEFAULT_PERMIT_ALL_URL = {};

  @NotNull
  private final Map<String, Set<String>> permissionMethodsMappings;
  @NotNull
  private final Map<String, Set<String>> rolePermissionsMappings;

  private String[] permitAllUrls = DEFAULT_PERMIT_ALL_URL;

  private String principalClaim = DEFAULT_PRINCIPAL_CLAIM;

  public Map<String, Set<String>> permissionsByRole() {
    return rolePermissionsMappings;
  }

  Map<String, Set<String>> permissionsByMethod() {
    return permissionMethodsMappings
        .entrySet()
        .stream()
        .flatMap(
            entry ->
                entry
                    .getValue()
                    .stream()
                    .map(s -> new SimpleEntry<>(entry.getKey(), s)))
        .collect(
            groupingBy(SimpleEntry::getValue,
            HashMap::new,
            mapping(SimpleEntry::getKey, toSet())));
  }
}
