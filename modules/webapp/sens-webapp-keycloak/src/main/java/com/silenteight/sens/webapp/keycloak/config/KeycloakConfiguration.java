package com.silenteight.sens.webapp.keycloak.config;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.*;
import org.keycloak.representations.adapters.action.GlobalRequestResult;
import org.keycloak.representations.adapters.config.AdapterConfig;
import org.keycloak.representations.idm.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import javax.ws.rs.core.Response;

@Configuration
@EnableConfigurationProperties(KeycloakConfigurationProperties.class)
public class KeycloakConfiguration {

  public static final String KEYCLOAK_ADMIN_CLIENT = "keycloakAdminClient";
  public static final String KEYCLOAK_WEBAPP_CLIENT = "keycloakWebappClient";

  @Bean(KEYCLOAK_ADMIN_CLIENT)
  Keycloak keycloakAdminClient(KeycloakAdminClientFactory keycloakAdminClientFactory) {
    return keycloakAdminClientFactory.getAdminClient();
  }

  //@Bean(KEYCLOAK_WEBAPP_CLIENT)
  //@DependsOn(IMPORT_KEYCLOAK_CONFIG_BEAN)
  Keycloak keycloakWebappClient(AdapterConfig adapterConfig) {
    return KeycloakBuilder.builder()
        .clientId(adapterConfig.getResource())
        .clientSecret((String) adapterConfig.getCredentials().get("secret"))
        .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
        .realm(adapterConfig.getRealm())
        .serverUrl(adapterConfig.getAuthServerUrl())
        .build();
  }

  @Bean
  RealmResource realmResource() {
    return new RealmResource() {
      @Override
      public RealmRepresentation toRepresentation() {
        return null;
      }

      @Override
      public void update(RealmRepresentation realmRepresentation) {

      }

      @Override
      public ClientsResource clients() {
        return null;
      }

      @Override
      public ClientScopesResource clientScopes() {
        return null;
      }

      @Override
      public List<ClientScopeRepresentation> getDefaultDefaultClientScopes() {
        return null;
      }

      @Override
      public void addDefaultDefaultClientScope(String clientScopeId) {

      }

      @Override
      public void removeDefaultDefaultClientScope(String clientScopeId) {

      }

      @Override
      public List<ClientScopeRepresentation> getDefaultOptionalClientScopes() {
        return null;
      }

      @Override
      public void addDefaultOptionalClientScope(String clientScopeId) {

      }

      @Override
      public void removeDefaultOptionalClientScope(String clientScopeId) {

      }

      @Override
      public ClientRepresentation convertClientDescription(
          String description) {
        return null;
      }

      @Override
      public UsersResource users() {
        return null;
      }

      @Override
      public RolesResource roles() {
        return null;
      }

      @Override
      public RoleByIdResource rolesById() {
        return null;
      }

      @Override
      public GroupsResource groups() {
        return null;
      }

      @Override
      public void clearEvents() {

      }

      @Override
      public List<EventRepresentation> getEvents() {
        return null;
      }

      @Override
      public List<EventRepresentation> getEvents(
          List<String> types, String client, String user, String dateFrom, String dateTo,
          String ipAddress, Integer firstResult, Integer maxResults) {
        return null;
      }

      @Override
      public void clearAdminEvents() {

      }

      @Override
      public List<AdminEventRepresentation> getAdminEvents() {
        return null;
      }

      @Override
      public List<AdminEventRepresentation> getAdminEvents(
          List<String> operationTypes, String authRealm, String authClient, String authUser,
          String authIpAddress, String resourcePath, String dateFrom, String dateTo,
          Integer firstResult, Integer maxResults) {
        return null;
      }

      @Override
      public RealmEventsConfigRepresentation getRealmEventsConfig() {
        return null;
      }

      @Override
      public void updateRealmEventsConfig(
          RealmEventsConfigRepresentation rep) {

      }

      @Override
      public GroupRepresentation getGroupByPath(String path) {
        return null;
      }

      @Override
      public List<GroupRepresentation> getDefaultGroups() {
        return null;
      }

      @Override
      public void addDefaultGroup(String groupId) {

      }

      @Override
      public void removeDefaultGroup(String groupId) {

      }

      @Override
      public IdentityProvidersResource identityProviders() {
        return null;
      }

      @Override
      public void remove() {

      }

      @Override
      public List<Map<String, String>> getClientSessionStats() {
        return null;
      }

      @Override
      public ClientInitialAccessResource clientInitialAccess() {
        return null;
      }

      @Override
      public ClientRegistrationPolicyResource clientRegistrationPolicy() {
        return null;
      }

      @Override
      public Response partialImport(
          PartialImportRepresentation rep) {
        return null;
      }

      @Override
      public RealmRepresentation partialExport(
          Boolean exportGroupsAndRoles, Boolean exportClients) {
        return null;
      }

      @Override
      public AuthenticationManagementResource flows() {
        return null;
      }

      @Override
      public AttackDetectionResource attackDetection() {
        return null;
      }

      @Override
      public Response testLDAPConnection(
          String action, String connectionUrl, String bindDn, String bindCredential,
          String useTruststoreSpi, String connectionTimeout) {
        return null;
      }

      @Override
      public Response testSMTPConnection(String config) throws Exception {
        return null;
      }

      @Override
      public void clearRealmCache() {

      }

      @Override
      public void clearUserCache() {

      }

      @Override
      public void clearKeysCache() {

      }

      @Override
      public GlobalRequestResult pushRevocation() {
        return null;
      }

      @Override
      public GlobalRequestResult logoutAll() {
        return null;
      }

      @Override
      public void deleteSession(String sessionId) {

      }

      @Override
      public ComponentsResource components() {
        return null;
      }

      @Override
      public UserStorageProviderResource userStorage() {
        return null;
      }

      @Override
      public KeyResource keys() {
        return null;
      }
    };
  }

  @Bean
  UsersResource usersResource(RealmResource realmResource) {
    return new UsersResource() {
      @Override
      public List<UserRepresentation> search(
          String username, String firstName, String lastName, String email, Integer firstResult,
          Integer maxResults) {
        return null;
      }

      @Override
      public List<UserRepresentation> search(
          String username, String firstName, String lastName, String email, Integer firstResult,
          Integer maxResults, Boolean briefRepresentation) {
        return null;
      }

      @Override
      public List<UserRepresentation> search(String username) {
        return null;
      }

      @Override
      public List<UserRepresentation> search(
          String search, Integer firstResult, Integer maxResults) {
        return null;
      }

      @Override
      public List<UserRepresentation> search(
          String search, Integer firstResult, Integer maxResults, Boolean briefRepresentation) {
        return null;
      }

      @Override
      public List<UserRepresentation> list(Integer firstResult, Integer maxResults) {
        return null;
      }

      @Override
      public List<UserRepresentation> list() {
        return null;
      }

      @Override
      public Response create(UserRepresentation userRepresentation) {
        return null;
      }

      @Override
      public Integer count() {
        return null;
      }

      @Override
      public UserResource get(String id) {
        return null;
      }

      @Override
      public Response delete(String id) {
        return null;
      }
    };
  }

  @Bean
  RolesResource rolesResource(RealmResource realmResource) {
    return new RolesResource() {
      @Override
      public List<RoleRepresentation> list() {
        return null;
      }

      @Override
      public void create(RoleRepresentation roleRepresentation) {

      }

      @Override
      public RoleResource get(String roleName) {
        return null;
      }

      @Override
      public void deleteRole(String roleName) {

      }
    };
  }
}
