package com.silenteight.sep.usermanagement.keycloak.sso;

import com.silenteight.sep.usermanagement.api.dto.RoleMappingDto;
import com.silenteight.sep.usermanagement.api.dto.RolesDto;
import com.silenteight.sep.usermanagement.api.dto.SsoAttributeDto;
import com.silenteight.sep.usermanagement.keycloak.BaseKeycloakIntegrationTest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.keycloak.representations.idm.IdentityProviderRepresentation;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static java.util.Set.copyOf;
import static java.util.Set.of;
import static org.assertj.core.api.Assertions.*;
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
    Map<String, RoleMappingDto> ssoMappings = Map.of(
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
          .isEqualTo(copyOf(ssoMappings.get(m.getName()).getRolesDto().getRoles()));

    });

    //when
    assertNotNull(underTest.getMapping("mapping 01"));
    assertNotNull(underTest.getMapping("mappingToRemove"));
    assertNotNull(underTest.getMapping("mapping 03"));
    assertNotNull(underTest.getMapping("mapping 04"));
    //then

    //when
    underTest.deleteMapping("mappingToRemove");
    //and
    actualMappings = underTest.listDefaultIdpMappings();

    //then
    assertThat(actualMappings.size()).isEqualTo(ssoMappings.size() - 1);
    assertFalse(actualMappings.stream().anyMatch(m -> m.getName().equals("mappingToRemove")));
  }

  @Test
  void shouldNotCreateMappingWhenAlreadyExists() {
    //given
    Map<String, RoleMappingDto> ssoMappings = Map.of(
        "mapping 01", createRoleMappingDto("mapping 01"),
        "existingMapping", createRoleMappingDto("existingMapping"),
        "mapping 03", createRoleMappingDto("mapping 03"),
        "mapping 04", createRoleMappingDto("mapping 04"));
    //and
    ssoMappings.values().forEach(dto -> underTest.addMapping(dto));

    //expect
    assertThrows(SsoRoleMapperAlreadyExistException.class, () ->
        underTest.addMapping(createRoleMappingDto("existingMapping")));
  }

  @Test
  void shouldReturnMappingsForTheFirstIdentityProviders() throws IOException {
    //given
    String expectedProviderName = "first-saml";
    createIdentityProvider(expectedProviderName,"keycloak-first-saml-idp-config.json");
    //and
    RoleMappingDto dto = createRoleMappingDto("test mapping");
    dto.setProviderAlias(expectedProviderName);
    //and
    underTest.addMapping(dto);

    //when
    List<RoleMappingDto> mappings = underTest.listDefaultIdpMappings();

    //then
    assertThat(mappings.size()).isEqualTo(1);
    assertThat(mappings.get(0).getProviderAlias()).isEqualTo(expectedProviderName);
    assertThat(mappings.get(0).getName()).isEqualTo("test mapping");
  }

  private RoleMappingDto createRoleMappingDto(String mappingName) {
    return RoleMappingDto.builder()
        .name(mappingName)
        .ssoAttributes(of(
            SsoAttributeDto.builder().key("saml_attr_01").value("saml_attr_val_01").build(),
            SsoAttributeDto.builder().key("saml_attr_02").value("saml_attr_val_02").build()
        ))
        .rolesDto(new RolesDto(ImmutableList.of("create policy", "governance.uma_protection")))
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
