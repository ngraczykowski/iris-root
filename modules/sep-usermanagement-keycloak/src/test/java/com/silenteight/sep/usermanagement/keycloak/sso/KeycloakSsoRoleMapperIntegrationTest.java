package com.silenteight.sep.usermanagement.keycloak.sso;

import com.silenteight.sep.usermanagement.api.identityprovider.dto.CreateRoleMappingDto;
import com.silenteight.sep.usermanagement.api.identityprovider.dto.RoleMappingDto;
import com.silenteight.sep.usermanagement.api.identityprovider.dto.SsoAttributeDto;
import com.silenteight.sep.usermanagement.api.identityprovider.exception.SsoRoleMapperAlreadyExistsException;
import com.silenteight.sep.usermanagement.api.role.dto.RolesDto;
import com.silenteight.sep.usermanagement.keycloak.BaseKeycloakIntegrationTest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.keycloak.admin.client.resource.IdentityProviderResource;
import org.keycloak.representations.idm.IdentityProviderMapperRepresentation;
import org.keycloak.representations.idm.IdentityProviderRepresentation;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.silenteight.sep.usermanagement.keycloak.sso.KeycloakRoleMapperTypes.SAML_ADVANCED_ROLE_IDP_MAPPER;
import static java.util.Set.copyOf;
import static java.util.Set.of;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class KeycloakSsoRoleMapperIntegrationTest extends BaseKeycloakIntegrationTest {

  private static final String IDENTITY_PROVIDER_NAME = "saml";
  private static final String KEYCLOAK_SAML_IDP_SPEC_JSON = "keycloak-saml-idp-configuration.json";
  private KeycloakSsoRoleMapper underTest;

  @BeforeEach
  void init() throws IOException {
    createIdentityProvider(IDENTITY_PROVIDER_NAME, KEYCLOAK_SAML_IDP_SPEC_JSON);
    underTest = new KeycloakSsoRoleMapper(getRealm().identityProviders(), new ObjectMapper());
  }

  @AfterEach
  void cleanUp() {
    deleteIdentityProvider(IDENTITY_PROVIDER_NAME);
  }

  @Test
  void shouldCreateListAndDeleteSsoRoleMappings() {
    //given
    final Map<String, CreateRoleMappingDto> ssoMappings = Map.of(
        "mapping 01", createRoleMappingDto("mapping 01"),
        "mappingToRemove", createRoleMappingDto("mappingToRemove"),
        "mapping 03", createRoleMappingDto("mapping 03"),
        "mapping 04", createRoleMappingDto("mapping 04"));

    //when
    ssoMappings.values().forEach(dto -> underTest.addMapping(dto));
    //and
    List<RoleMappingDto> actualMappings = underTest.listDefaultIdpMappings();
    //then
    assertThat(actualMappings.size()).isEqualTo(ssoMappings.size());
    //and
    actualMappings.forEach(m -> {
      assertThat(m.getProviderAlias()).isEqualTo(IDENTITY_PROVIDER_NAME);
      assertThat(m.getSsoAttributes()).isEqualTo(ssoMappings.get(m.getName()).getSsoAttributes());
      assertThat(copyOf(m.getRolesDto().getRoles()))
          .isEqualTo(copyOf(ssoMappings.get(m.getName()).getRoles().getRoles()));
    });

    //expect
    actualMappings.forEach(m -> assertNotNull(underTest.getMapping(m.getId())));

    //when
    underTest.deleteMapping(actualMappings.stream()
        .filter(m -> m.getName().equals("mappingToRemove")).findFirst().get().getId());
    //and
    actualMappings = underTest.listDefaultIdpMappings();

    //then
    assertThat(actualMappings.size()).isEqualTo(ssoMappings.size() - 1);
    assertFalse(actualMappings.stream().anyMatch(m -> m.getName().equals("mappingToRemove")));
  }

  @Test
  void shouldReturnLegacyRoleMappings() {
    //given
    IdentityProviderResource idpResource =
        getRealm().identityProviders().get(IDENTITY_PROVIDER_NAME);
    //and
    IdentityProviderMapperRepresentation mapper = new IdentityProviderMapperRepresentation();
    mapper.setIdentityProviderAlias(IDENTITY_PROVIDER_NAME);
    mapper.setIdentityProviderMapper(SAML_ADVANCED_ROLE_IDP_MAPPER);
    mapper.setName("Legacy mapper name");
    mapper.setConfig(Map.of(
        "syncMode", "FORCE",
        "are.attribute.values.regex", "",
        "attributes", "[{\"key\":\"saml_attr_01\",\"value\":\"saml_attr_val_01\"}]",
        "role", "administrator"));
    //and
    idpResource.addMapper(mapper);

    //when
    RoleMappingDto dto = underTest.listDefaultIdpMappings().get(0);

    //then
    assertNotNull(dto.getId());
    assertEquals(mapper.getName(), dto.getName());
    assertEquals(mapper.getIdentityProviderAlias(), dto.getProviderAlias());
    assertThat(dto.getSsoAttributes())
        .contains(new SsoAttributeDto("saml_attr_01", "saml_attr_val_01"));
    assertEquals(mapper.getConfig().get("role"), dto.getRolesDto().getRoles().get(0));
  }

  @Test
  void shouldReturnRoleMappingsWithEmptyRolesAndAttributes() {
    //given
    IdentityProviderResource idpResource =
        getRealm().identityProviders().get(IDENTITY_PROVIDER_NAME);
    //and
    IdentityProviderMapperRepresentation mapper = new IdentityProviderMapperRepresentation();
    mapper.setIdentityProviderAlias(IDENTITY_PROVIDER_NAME);
    mapper.setIdentityProviderMapper(SAML_ADVANCED_ROLE_IDP_MAPPER);
    mapper.setName("Legacy mapper name");
    mapper.setConfig(Map.of(
        "syncMode", "FORCE",
        "are.attribute.values.regex", ""));

    //and
    idpResource.addMapper(mapper);

    //when
    RoleMappingDto dto = underTest.listDefaultIdpMappings().get(0);

    //then
    assertNotNull(dto.getId());
    assertEquals(mapper.getName(), dto.getName());
    assertEquals(mapper.getIdentityProviderAlias(), dto.getProviderAlias());
    assertThat(dto.getSsoAttributes()).isEmpty();
    assertThat(dto.getRolesDto().getRoles()).isEmpty();
  }

  @Test
  void shouldNotCreateMappingWhenAlreadyExists() {
    //given
    CreateRoleMappingDto mappingAddedTwice = createRoleMappingDto("existingMapping");
    Map<String, CreateRoleMappingDto> ssoMappings = Map.of(
        "mapping 01", createRoleMappingDto("mapping 01"),
        "existingMapping", mappingAddedTwice,
        "mapping 03", createRoleMappingDto("mapping 03"),
        "mapping 04", createRoleMappingDto("mapping 04"));
    //and
    ssoMappings.values().forEach(dto -> underTest.addMapping(dto));

    //expect
    assertThrows(SsoRoleMapperAlreadyExistsException.class, () ->
        underTest.addMapping(mappingAddedTwice));
  }

  @Test
  void shouldReturnMappingsForTheFirstIdentityProvider() throws IOException {
    //given
    String expectedProviderName = "first-saml";
    createIdentityProvider(expectedProviderName, "keycloak-first-saml-idp-config.json");
    //and
    CreateRoleMappingDto dto = createRoleMappingDto("test mapping");
    //and
    underTest.addMapping(dto);

    //when
    List<RoleMappingDto> mappings = underTest.listDefaultIdpMappings();

    //then
    assertThat(mappings.size()).isEqualTo(1);
    assertThat(mappings.get(0).getProviderAlias()).isEqualTo(expectedProviderName);
    assertThat(mappings.get(0).getName()).isEqualTo("test mapping");
  }

  private CreateRoleMappingDto createRoleMappingDto(String mappingName) {
    return CreateRoleMappingDto.builder()
        .name(mappingName)
        .ssoAttributes(of(
            SsoAttributeDto.builder().key("saml_attr_01").value("saml_attr_val_01").build(),
            SsoAttributeDto.builder().key("saml_attr_02").value("saml_attr_val_02").build(),
            SsoAttributeDto.builder().key("saml_attr_01").value("saml_attr_val_03").build()
        ))
        .roles(new RolesDto(ImmutableList.of("create policy", "governance.uma_protection")))
        .build();
  }

  private void createIdentityProvider(String idpName, String configFile) throws IOException {
    IdentityProviderRepresentation idp = new IdentityProviderRepresentation();
    idp.setEnabled(true);
    idp.setAlias(idpName);
    idp.setProviderId("saml");

    Map<String, String> configuration = (new ObjectMapper()).readValue(
        new File(getClass().getClassLoader()
            .getResource(configFile).getFile()),
        new TypeReference<Map<String, String>>() {});

    idp.setConfig(configuration);
    getRealm().identityProviders().create(idp);
  }

  private void deleteIdentityProvider(String idpName) {
    getRealm().identityProviders().get(idpName).remove();
  }
}
