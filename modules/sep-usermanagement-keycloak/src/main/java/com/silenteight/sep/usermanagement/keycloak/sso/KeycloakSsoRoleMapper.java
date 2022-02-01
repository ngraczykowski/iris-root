package com.silenteight.sep.usermanagement.keycloak.sso;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.usermanagement.api.IdentityProviderRoleMapper;
import com.silenteight.sep.usermanagement.api.dto.CreateRoleMappingDto;
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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import javax.ws.rs.NotFoundException;

import static com.silenteight.sep.usermanagement.keycloak.logging.LogMarkers.USER_MANAGEMENT;
import static com.silenteight.sep.usermanagement.keycloak.sso.KeycloakRoleMapperTypes.SAML_ADVANCED_ROLE_IDP_MAPPER;
import static com.silenteight.sep.usermanagement.keycloak.sso.SsoMappingNameResolver.build;
import static com.silenteight.sep.usermanagement.keycloak.sso.SsoMappingNameResolver.extractId;
import static com.silenteight.sep.usermanagement.keycloak.sso.SsoMappingNameResolver.extractSharedName;
import static com.silenteight.sep.usermanagement.keycloak.sso.SsoMappingNameResolver.isLegacyName;
import static java.util.Collections.emptySet;
import static java.util.Comparator.comparing;
import static java.util.List.of;
import static java.util.Optional.ofNullable;
import static java.util.UUID.fromString;
import static java.util.UUID.randomUUID;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
public class KeycloakSsoRoleMapper implements IdentityProviderRoleMapper {

  private static final String DEFAULT_SYNC_MODE = "FORCE";

  @NonNull
  private final IdentityProvidersResource identityProvidersResource;

  @NonNull
  private ObjectMapper sepUserManagementKeycloakObjectMapper;

  @Override
  public List<RoleMappingDto> listDefaultIdpMappings() {
    return collectMappers(getDefaultProvider().getMappers());
  }

  @Override
  public List<RoleMappingDto> listMappings(String providerAlias) {
    return collectMappers(getProviderByName(providerAlias).getMappers());
  }

  @Override
  public void addMapping(CreateRoleMappingDto dto) throws KeycloakException {
    log.info(USER_MANAGEMENT, "Adding SSO role mapping for identity provider={}, mapping name={}",
        dto.getName());

    final IdentityProviderResource idpResource = getDefaultProvider();
    findMappingByName(idpResource, dto.getName()).ifPresentOrElse(m -> {
      throw new SsoRoleMapperAlreadyExistException(dto.getName());
    }, () -> addKeycloakMappings(idpResource, dto));
  }

  @Override
  public void deleteMapping(UUID mappingId) {
    log.info(USER_MANAGEMENT,
        "Deleting SSO role mapping with aggregate id={}", mappingId);

    IdentityProviderResource idpResource = getDefaultProvider();
    List<IdentityProviderMapperRepresentation> mappers =
        findKeycloakMappersBySharedId(idpResource, mappingId);

    if (mappers.isEmpty()) {
      deleteLegacyMapper(idpResource, mappingId);
    } else {
      mappers.forEach(m -> idpResource.delete(m.getId()));
    }
  }

  @Override
  public RoleMappingDto getMapping(UUID mappingId) throws KeycloakException {
    log.info(USER_MANAGEMENT, "Retrieving SSO role mapping with aggregate id={}", mappingId);

    IdentityProviderResource idpResource = getDefaultProvider();
    List<IdentityProviderMapperRepresentation> keycloakMappers =
        findKeycloakMappersBySharedId(idpResource, mappingId);

    if (keycloakMappers.isEmpty()) {
      keycloakMappers = of(findLegacyMapper(idpResource, mappingId)
          .orElseThrow(() -> new SsoRoleMapperNotFoundException(mappingId.toString())));
    }
    return buildRoleMappingDto(keycloakMappers);
  }

  private List<RoleMappingDto> collectMappers(
      List<IdentityProviderMapperRepresentation> keycloakMappers) {

    return keycloakMappers.stream()
        .collect(groupingBy(r -> extractSharedName(r.getName())))
        .entrySet()
        .stream()
        .map(e -> buildRoleMappingDto(e.getValue()))
        .sorted(comparing(RoleMappingDto::getName))
        .collect(toList());
  }

  private RoleMappingDto buildRoleMappingDto(
       List<IdentityProviderMapperRepresentation> keycloakMappers) {

    IdentityProviderMapperRepresentation mapper = keycloakMappers.stream().findAny().get();
    final String keycloakMapperName = mapper.getName();
    boolean legacy = isLegacyName(keycloakMapperName);
    RoleMappingDto dto = RoleMappingDto.builder()
        .providerAlias(mapper.getIdentityProviderAlias())
        .id(fromString(legacy ? mapper.getId() : extractId(keycloakMapperName)))
        .name(extractSharedName(keycloakMapperName)).build();

    keycloakMappers.stream()
        .map(IdentityProviderMapperRepresentation::getConfig)
        .forEach(config -> {
          dto.addSsoAttributes(attrsFromString(config.get("attributes")));
          dto.addTargetRole(config.get("role"));
        });

    return dto;
  }

  private void addKeycloakMappings(IdentityProviderResource idpResource, CreateRoleMappingDto dto) {
    UUID aggregateMappingId = randomUUID();
    dto.getRoles()
        .getRoles()
        .forEach(withCounter((mappingIndex, role) -> {
          idpResource.addMapper(
              createKeycloakRoleMapper(mappingIndex,
                  idpResource.toRepresentation().getAlias(), dto.getName(),
                  aggregateMappingId, dto.getSsoAttributes(), role));
        }));
  }

  private IdentityProviderMapperRepresentation createKeycloakRoleMapper(Integer subMappingIndex,
      String providerAlias, String name, UUID sharedMappingId,
      Set<SsoAttributeDto> ssoAttributes, String targetRole) {

    IdentityProviderMapperRepresentation mapper = new IdentityProviderMapperRepresentation();
    mapper.setIdentityProviderAlias(providerAlias);
    mapper.setIdentityProviderMapper(SAML_ADVANCED_ROLE_IDP_MAPPER);
    mapper.setName(build(name, sharedMappingId, subMappingIndex));
    mapper.setConfig(Map.of(
          "syncMode", DEFAULT_SYNC_MODE,
          "are.attribute.values.regex", "",
          "attributes", attrsToString(ssoAttributes),
          "role", targetRole));
    return mapper;
  }

  private IdentityProviderResource getProviderByName(String providerName) {
    return ofNullable(identityProvidersResource.get(providerName))
        .orElseThrow(() -> new IdentityProviderNotFoundException(providerName));
  }

  private IdentityProviderResource getDefaultProvider() {
    IdentityProviderRepresentation idp = identityProvidersResource.findAll()
        .stream()
        .sorted(comparing(IdentityProviderRepresentation::getAlias))
        .findFirst()
        .orElseThrow(IdentityProviderNotFoundException::new);

    return getProviderByName(idp.getAlias());
  }

  private String attrsToString(Set<SsoAttributeDto> ssoAttributes) {
    try {
      return sepUserManagementKeycloakObjectMapper.writeValueAsString(ssoAttributes);
    } catch (JsonProcessingException e) {
      log.error(USER_MANAGEMENT,
          "Could not convert saml SSO attributes to keycloak representation.", e);
      return "[]";
    }
  }

  private Set<SsoAttributeDto> attrsFromString(String attributes) {
    try {
      return sepUserManagementKeycloakObjectMapper
          .readValue(attributes, new TypeReference<Set<SsoAttributeDto>>(){});
    } catch (JsonProcessingException e) {
      log.error(USER_MANAGEMENT,
          "Could not convert keycloak SSO attributes to SsoAttribute collection.", e);
      return emptySet();
    }
  }

  private Optional<IdentityProviderMapperRepresentation> findMappingByName(
      IdentityProviderResource idpResource, String mappingName) {

    return idpResource.getMappers().stream()
        .filter(m -> mappingName.equals(extractSharedName(m.getName())))
        .findFirst();
  }

  private List<IdentityProviderMapperRepresentation> findKeycloakMappersBySharedId(
      IdentityProviderResource idpResource, UUID aggregateId) {

    return idpResource
        .getMappers()
        .stream()
        .filter(m -> aggregateId.toString().equals(extractId(m.getName()))).collect(toList());
  }

  private Optional<IdentityProviderMapperRepresentation> findLegacyMapper(
      IdentityProviderResource idpResource, UUID mappingId) {

    try {
      return Optional.of(idpResource.getMapperById(mappingId.toString()));
    } catch (NotFoundException e) {
      return Optional.empty();
    }
  }

  private void deleteLegacyMapper(IdentityProviderResource idpResource, UUID mappingId) {
    try {
      idpResource.delete(mappingId.toString());
    } catch (NotFoundException e) {
      log.info(USER_MANAGEMENT, "Cannot delete mapping id={} because it is not found.", mappingId);
    }
  }

  private <T> Consumer<T> withCounter(BiConsumer<Integer, T> consumer) {
    AtomicInteger counter = new AtomicInteger(0);
    return item -> consumer.accept(counter.getAndIncrement(), item);
  }
}