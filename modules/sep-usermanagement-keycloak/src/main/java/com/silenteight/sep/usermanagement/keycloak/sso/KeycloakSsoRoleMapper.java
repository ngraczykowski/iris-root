package com.silenteight.sep.usermanagement.keycloak.sso;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.usermanagement.api.IdentityProviderRoleMapper;
import com.silenteight.sep.usermanagement.api.dto.RoleMappingDto;
import com.silenteight.sep.usermanagement.api.dto.SsoAttributeDto;
import com.silenteight.sep.usermanagement.keycloak.KeycloakException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.keycloak.admin.client.resource.IdentityProviderResource;
import org.keycloak.admin.client.resource.IdentityProvidersResource;
import org.keycloak.representations.idm.IdentityProviderMapperRepresentation;
import org.keycloak.representations.idm.IdentityProviderRepresentation;

import java.util.*;

import static com.silenteight.sep.usermanagement.keycloak.logging.LogMarkers.USER_MANAGEMENT;
import static com.silenteight.sep.usermanagement.keycloak.sso.KeycloakRoleMapperTypes.SAML_ADVANCED_ROLE_IDP_MAPPER;
import static java.util.Collections.emptySet;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.substringBefore;

@Slf4j
@RequiredArgsConstructor
public class KeycloakSsoRoleMapper implements IdentityProviderRoleMapper {

  private static final String DEFAULT_SYNC_MODE = "FORCE";
  private static final String UUID_SEPARATOR = "_UUID_";

  @NonNull
  private final IdentityProvidersResource identityProvidersResource;

  @NonNull
  private ObjectMapper sepUserManagementKeycloakObjectMapper;

  @Override
  public List<RoleMappingDto> listDefaultIdpMappings() {
    return listMappings(getDefaultProviderAlias());
  }

  @Override
  public List<RoleMappingDto> listMappings(String providerAlias) {

    return getProvider(providerAlias)
        .getMappers()
        .stream()
        .collect(groupingBy(r -> trimUuidPostfix(r.getName())))
        .entrySet()
        .stream()
        .map(e -> buildAggregateMappingDto(providerAlias, e.getKey(), e.getValue()))
        .collect(toList());
  }

  @Override
  public void addMapping(RoleMappingDto dto) throws KeycloakException {
    log.info(USER_MANAGEMENT, "Adding SSO role mapping for identity provider={}, mapping name={}",
        dto.getProviderAlias(), dto.getName());

    String providerAlias = resolveAlias(dto.getProviderAlias());
    if (mappingExists(providerAlias, dto.getName())) {
      throw new SsoRoleMapperAlreadyExistException(dto.getName());
    }

    addKeycloakMappings(providerAlias, dto);
  }

  @Override
  public void deleteMapping(String mappingName) {
    log.info(USER_MANAGEMENT, "Deleting SSO role mapping with name={}", mappingName);

    final String defaultProviderAlias = getDefaultProviderAlias();
    final IdentityProviderResource idpResource = getProvider(defaultProviderAlias);
    findKeycloakMappersByPrefix(defaultProviderAlias, mappingName)
        .forEach(m -> idpResource.delete(m.getId()));
  }

  @Override
  public RoleMappingDto getMapping(String mappingName) throws KeycloakException {
    log.info(USER_MANAGEMENT, "Retrieving SSO role mapping with name={}", mappingName);

    final String defaultProviderAlias = getDefaultProviderAlias();
    List<IdentityProviderMapperRepresentation> keycloakMappers = findKeycloakMappersByPrefix(
        defaultProviderAlias, mappingName);

    if (keycloakMappers.isEmpty()) {
      throw new SsoRoleMapperNotFoundException(mappingName);
    }

    return buildAggregateMappingDto(defaultProviderAlias, mappingName, keycloakMappers);
  }

  private IdentityProviderResource getProvider(String providerName) {
    return ofNullable(identityProvidersResource.get(providerName))
            .orElseThrow(() -> new IdentityProviderNotFoundException(providerName));
  }

  private String resolveAlias(String provided) {
    return isBlank(provided) ? getDefaultProviderAlias() : provided;
  }

  private String getDefaultProviderAlias() {
    IdentityProviderRepresentation idp = identityProvidersResource.findAll()
        .stream()
        .sorted(Comparator.comparing(IdentityProviderRepresentation::getAlias))
        .findFirst()
        .orElseThrow(IdentityProviderNotFoundException::new);

    return idp.getAlias();
  }

  private RoleMappingDto buildAggregateMappingDto(String providerAlias, String mappingName,
       List<IdentityProviderMapperRepresentation> keycloakMappers) {

    RoleMappingDto dto  = RoleMappingDto.builder()
        .providerAlias(providerAlias)
        .name(mappingName).build();

    keycloakMappers.stream()
        .map(IdentityProviderMapperRepresentation::getConfig)
        .forEach(config -> {
          dto.addSsoAttributes(attrsFromString(config.get("attributes")));
          dto.addTargetRole(config.get("role"));
        });

    return dto;
  }

  private void addKeycloakMappings(String providerAlias, RoleMappingDto dto) {
    final IdentityProviderResource identityProvider = getProvider(providerAlias);
    dto.getRolesDto()
        .getRoles()
        .forEach(role -> {
          identityProvider.addMapper(
              createKeycloakRoleMapper(
                  providerAlias, dto.getName(), dto.getSsoAttributes(), role));
        });
  }

  private IdentityProviderMapperRepresentation createKeycloakRoleMapper(String providerAlias,
      String name, Set<SsoAttributeDto> ssoAttributes, String targetRole) {

    IdentityProviderMapperRepresentation mapper = new IdentityProviderMapperRepresentation();
    mapper.setIdentityProviderAlias(providerAlias);
    mapper.setIdentityProviderMapper(SAML_ADVANCED_ROLE_IDP_MAPPER);
    mapper.setName(appendUuidPostfix(name));
    mapper.setConfig(Map.of(
          "syncMode", DEFAULT_SYNC_MODE,
          "are.attribute.values.regex", "",
          "attributes", attrsToString(ssoAttributes),
          "role", targetRole));
    return mapper;
  }

  private String trimUuidPostfix(String keycloakMapperUniqueName) {
    return substringBefore(keycloakMapperUniqueName, UUID_SEPARATOR);
  }

  private String appendUuidPostfix(String mapperName) {
    return mapperName + UUID_SEPARATOR + UUID.randomUUID();
  }

  private String attrsToString(Set<SsoAttributeDto> ssoAttributes) {
    try {
      return sepUserManagementKeycloakObjectMapper.writeValueAsString(ssoAttributes);
    } catch (JsonProcessingException e) {
      log.error("Could not convert saml SSO attributes to keycloak representation.", e);
      return "[]";
    }
  }

  private Set<SsoAttributeDto> attrsFromString(String attributes) {
    try {
      return sepUserManagementKeycloakObjectMapper
          .readValue(attributes, new TypeReference<Set<SsoAttributeDto>>(){});
    } catch (JsonProcessingException e) {
      log.error("Could not convert keycloak SSO attributes to SsoAttribute collection.", e);
      return emptySet();
    }
  }

  private boolean mappingExists(String providerAlias, String mappingName) {
    return getProvider(providerAlias).getMappers().stream().anyMatch(m ->
        mappingName.equals(trimUuidPostfix(m.getName())));
  }

  private List<IdentityProviderMapperRepresentation> findKeycloakMappersByPrefix(
      String providerAlias, String prefix) {

    return getProvider(providerAlias)
        .getMappers()
        .stream()
        .filter(m -> prefix.equals(trimUuidPostfix(m.getName())))
        .collect(toList());
  }
}