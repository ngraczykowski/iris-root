package com.silenteight.sep.usermanagement.keycloak.sso;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.usermanagement.api.identityprovider.IdentityProviderRoleMapper;
import com.silenteight.sep.usermanagement.api.identityprovider.dto.CreateRoleMappingDto;
import com.silenteight.sep.usermanagement.api.identityprovider.dto.RoleMappingDto;
import com.silenteight.sep.usermanagement.api.identityprovider.dto.SsoAttributeDto;
import com.silenteight.sep.usermanagement.api.identityprovider.exception.SsoRoleMapperAlreadyExistsException;
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
import static com.silenteight.sep.usermanagement.keycloak.sso.KeycloakMapperFinder.findKeycloakMappersBySharedId;
import static com.silenteight.sep.usermanagement.keycloak.sso.KeycloakRoleMapperTypes.SAML_ADVANCED_ROLE_IDP_MAPPER;
import static com.silenteight.sep.usermanagement.keycloak.sso.SsoMappingNameResolver.build;
import static com.silenteight.sep.usermanagement.keycloak.sso.SsoMappingNameResolver.extractId;
import static com.silenteight.sep.usermanagement.keycloak.sso.SsoMappingNameResolver.extractSharedName;
import static com.silenteight.sep.usermanagement.keycloak.sso.SsoMappingNameResolver.isLegacyName;
import static java.util.Collections.emptySet;
import static java.util.Comparator.comparing;
import static java.util.List.of;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import static java.util.UUID.fromString;
import static java.util.UUID.randomUUID;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
public class KeycloakSsoRoleMapper implements IdentityProviderRoleMapper {

  private static final String DEFAULT_SYNC_MODE = "FORCE";
  private static final String ATTRIBUTES = "attributes";
  private static final String ROLE = "role";

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
  public RoleMappingDto addMapping(CreateRoleMappingDto dto) throws KeycloakException {
    log.info(USER_MANAGEMENT, "Adding SSO role mapping with name={}", dto.getName());

    final IdentityProviderResource idpResource = getDefaultProvider();
    if (exists(idpResource, dto.getName())) {
      throw new SsoRoleMapperAlreadyExistsException(dto.getName());
    }
    UUID uuid = addKeycloakMappings(idpResource, dto);
    return getMapping(uuid, idpResource);
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

    return getMapping(mappingId, getDefaultProvider());
  }

  private RoleMappingDto getMapping(UUID mappingId, IdentityProviderResource idpResource) {
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
        .values()
        .stream()
        .map(this::buildRoleMappingDto)
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
          addAttributesToRoleMappingDto(dto, config.get(ATTRIBUTES));
          addRoleToRoleMappingDto(dto, config.get(ROLE));
        });

    return dto;
  }

  private UUID addKeycloakMappings(IdentityProviderResource idpResource, CreateRoleMappingDto dto) {
    UUID aggregateMappingId = randomUUID();
    dto.getRoles()
        .getRoles()
        .forEach(withCounter((mappingIndex, role) -> idpResource.addMapper(
            createKeycloakRoleMapper(mappingIndex,
                idpResource.toRepresentation().getAlias(), dto.getName(),
                aggregateMappingId, dto.getSsoAttributes(), role))));
    return aggregateMappingId;
  }

  private IdentityProviderMapperRepresentation createKeycloakRoleMapper(
      Integer subMappingIndex,
      String providerAlias, String name, UUID sharedMappingId,
      Set<SsoAttributeDto> ssoAttributes, String targetRole) {

    IdentityProviderMapperRepresentation mapper = new IdentityProviderMapperRepresentation();
    mapper.setIdentityProviderAlias(providerAlias);
    mapper.setIdentityProviderMapper(SAML_ADVANCED_ROLE_IDP_MAPPER);
    mapper.setName(build(name, sharedMappingId, subMappingIndex));
    mapper.setConfig(Map.of(
        "syncMode", DEFAULT_SYNC_MODE,
        "are.attribute.values.regex", "",
        ATTRIBUTES, attrsToString(ssoAttributes),
        ROLE, targetRole));
    return mapper;
  }

  private IdentityProviderResource getProviderByName(String providerName) {
    return ofNullable(identityProvidersResource.get(providerName))
        .orElseThrow(() -> new IdentityProviderNotFoundException(providerName));
  }

  private IdentityProviderResource getDefaultProvider() {
    IdentityProviderRepresentation idp = identityProvidersResource.findAll()
        .stream().min(comparing(IdentityProviderRepresentation::getAlias))
        .orElseThrow(IdentityProviderNotFoundException::new);

    return getProviderByName(idp.getAlias());
  }

  private void addAttributesToRoleMappingDto(RoleMappingDto dto, String attributes) {
    dto.addSsoAttributes(attrsFromString(attributes));
  }

  private static void addRoleToRoleMappingDto(RoleMappingDto dto, String role) {
    ofNullable(role).ifPresent(dto::addTargetRole);
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
    return ofNullable(attributes)
        .map(this::extractAttributes)
        .orElse(emptySet());
  }

  private Set<SsoAttributeDto> extractAttributes(String attributes) {
    try {
      return sepUserManagementKeycloakObjectMapper.readValue(attributes, new TypeReference<>() {});
    } catch (JsonProcessingException e) {
      log.error(USER_MANAGEMENT,
          "Could not convert keycloak SSO attributes to SsoAttribute collection.", e);

      return emptySet();
    }
  }

  private static boolean exists(IdentityProviderResource idpResource, String mappingName) {
    return idpResource.getMappers().stream()
        .anyMatch(m -> mappingName.equals(extractSharedName(m.getName())));
  }

  private Optional<IdentityProviderMapperRepresentation> findLegacyMapper(
      IdentityProviderResource idpResource, UUID mappingId) {

    try {
      return Optional.of(idpResource.getMapperById(mappingId.toString()));
    } catch (NotFoundException e) {
      return empty();
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
