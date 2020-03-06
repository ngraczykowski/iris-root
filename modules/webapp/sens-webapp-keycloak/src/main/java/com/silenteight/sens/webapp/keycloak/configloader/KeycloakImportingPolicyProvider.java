package com.silenteight.sens.webapp.keycloak.configloader;

import org.keycloak.representations.idm.PartialImportRepresentation.Policy;

@FunctionalInterface
public interface KeycloakImportingPolicyProvider {

  Policy getImportingPolicy();
}
