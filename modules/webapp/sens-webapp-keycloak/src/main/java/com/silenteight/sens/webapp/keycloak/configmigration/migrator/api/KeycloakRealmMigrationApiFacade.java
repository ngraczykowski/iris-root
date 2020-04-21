package com.silenteight.sens.webapp.keycloak.configmigration.migrator.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.audit.api.AuditLog;
import com.silenteight.sens.webapp.audit.api.AuditMarker;
import com.silenteight.sens.webapp.keycloak.configmigration.migrator.KeycloakRealmMigrationApi;

import org.keycloak.admin.client.resource.RealmsResource;
import org.keycloak.representations.idm.PartialImportRepresentation;
import org.keycloak.representations.idm.PartialImportRepresentation.Policy;
import org.keycloak.representations.idm.RealmRepresentation;

import java.util.function.Predicate;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;

import static com.silenteight.sens.webapp.logging.SensWebappLogMarkers.KEYCLOAK_MIGRATION;


@Slf4j
@RequiredArgsConstructor
class KeycloakRealmMigrationApiFacade implements KeycloakRealmMigrationApi {

  private final Policy importPolicy;
  private final RealmsResource realmsResource;
  private final AuditLog auditLog;

  @Override
  public void partialImport(
      String realmName, PartialImportRepresentation partialImportRepresentation) {
    partialImportRepresentation.setIfResourceExists(importPolicy.name());
    log.debug(KEYCLOAK_MIGRATION, "Performing Keycloak partial import. policy={}", importPolicy);
    Response response = realmsResource.realm(realmName)
        .partialImport(partialImportRepresentation);

    if (isNotSuccessful(response)) {
      auditLog.logError(
          AuditMarker.KEYCLOAK_MIGRATION, "Could not perform partial import. response={}",
          response.getStatusInfo());
      throw new FailedToPerformMigration(response);
    }
  }

  @Override
  public boolean realmExists(String realmName) {
    log.debug(KEYCLOAK_MIGRATION, "Checking if realm {} exists", realmName);
    Predicate<RealmRepresentation> sameName = realm -> realm.getRealm().equals(realmName);

    return realmsResource.findAll().stream().anyMatch(sameName);
  }

  @Override
  public void createRealm(RealmRepresentation realmRepresentation) {
    log.debug(KEYCLOAK_MIGRATION, "Creating realm {}", realmRepresentation.getRealm());

    realmsResource.create(realmRepresentation);
  }

  private static boolean isNotSuccessful(Response response) {
    return response.getStatusInfo().getFamily() != Family.SUCCESSFUL;
  }
}
