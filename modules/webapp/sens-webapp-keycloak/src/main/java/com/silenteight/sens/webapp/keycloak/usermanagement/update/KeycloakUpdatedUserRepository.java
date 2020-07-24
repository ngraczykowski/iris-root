package com.silenteight.sens.webapp.keycloak.usermanagement.update;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sens.webapp.audit.trace.AuditTracer;
import com.silenteight.sens.webapp.user.update.UpdatedUser;
import com.silenteight.sens.webapp.user.update.UpdatedUserRepository;

@RequiredArgsConstructor
class KeycloakUpdatedUserRepository implements UpdatedUserRepository {

  @NonNull
  private final KeycloakUserUpdater keycloakUserUpdater;
  @NonNull
  private final AuditTracer auditTracer;

  @Override
  public void save(UpdatedUser updatedUser) {
    try {
      keycloakUserUpdater.update(updatedUser);

      auditTracer.save(new UserUpdatedEvent(
          updatedUser.getUsername(), UpdatedUser.class.getName(), updatedUser));
    } catch (Exception e) {
      throw UserUpdateException.of(updatedUser.getUsername(), e);
    }
  }
}
