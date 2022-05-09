package com.silenteight.sep.usermanagement.keycloak.query;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.keycloak.representations.idm.UserRepresentation;

@RequiredArgsConstructor
@Slf4j
class UsersListFilter {

  private static final String FILTERING_ENABLED =
      "User {} will be hidden due to configured filtering.";
  @NonNull
  private final AttributeFilter attributeFilter;
  @NonNull
  private final RealmRoleFilter realmRoleFilter;

  boolean isVisible(UserRepresentation userRepresentation) {
    if (filterNotConfigured())
      return true;

    boolean isVisible = attributeFilter.visible(userRepresentation.getAttributes()) ||
        realmRoleFilter.visible(userRepresentation.getId());

    if (!isVisible)
      log.info(FILTERING_ENABLED, userRepresentation.getUsername());

    return isVisible;
  }

  private boolean filterNotConfigured() {
    return !attributeFilter.isConfigured() && !realmRoleFilter.isConfigured();
  }
}
