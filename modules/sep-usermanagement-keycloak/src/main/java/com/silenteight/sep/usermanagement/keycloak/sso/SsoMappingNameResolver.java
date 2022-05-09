package com.silenteight.sep.usermanagement.keycloak.sso;

import java.util.UUID;

import static org.apache.commons.lang3.StringUtils.substringAfter;
import static org.apache.commons.lang3.StringUtils.substringBefore;

public class SsoMappingNameResolver {

  private static final String ID_SEPARATOR = "_ID_";
  private static final String SUB_INDEX_SEP = "_SUB_";

  static String build(String name, UUID aggregateId, Integer mappingIndex) {
    return name + ID_SEPARATOR + aggregateId + SUB_INDEX_SEP + mappingIndex;
  }

  static boolean isLegacyName(String keycloakMapperName) {
    return !keycloakMapperName.matches(".*" + ID_SEPARATOR + ".*" + SUB_INDEX_SEP + "\\d+");
  }

  static String extractId(String keycloakMapperName) {
    return substringAfter(trimSubIndexPostfix(keycloakMapperName), ID_SEPARATOR);
  }

  static String extractSharedName(String keycloakMapperName) {
    return substringBefore(trimSubIndexPostfix(keycloakMapperName), ID_SEPARATOR);
  }

  static String trimSubIndexPostfix(String keycloakMapperUniqueName) {
    return substringBefore(keycloakMapperUniqueName, SUB_INDEX_SEP);
  }
}
