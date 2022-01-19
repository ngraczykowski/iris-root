package com.silenteight.sep.usermanagement.keycloak.sso;

import com.silenteight.sep.usermanagement.api.dto.RoleMappingDto;
import com.silenteight.sep.usermanagement.api.dto.RolesDto;
import com.silenteight.sep.usermanagement.api.dto.SsoAttributeDto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.keycloak.admin.client.resource.IdentityProviderResource;
import org.keycloak.admin.client.resource.IdentityProvidersResource;
import org.keycloak.representations.idm.IdentityProviderMapperRepresentation;
import org.keycloak.representations.idm.IdentityProviderRepresentation;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;
import java.util.UUID;

import static java.util.List.of;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class KeycloakSsoRoleMapperTest {

  @Mock
  private IdentityProvidersResource identityProvidersResource;

  @Mock
  private IdentityProviderResource identityProviderResource;

  private KeycloakSsoRoleMapper underTest;

  private static final String ID_PROVIDER_NAME = "Saml Identity Provider";

  @Captor
  private ArgumentCaptor<IdentityProviderMapperRepresentation> mapperCaptor;

  @Captor
  private ArgumentCaptor<String> mapperIdCaptor;

  @BeforeEach
  void setUp() {
    underTest = new KeycloakSsoRoleMapper(identityProvidersResource, new ObjectMapper());
    given(identityProvidersResource.get(ID_PROVIDER_NAME)).willReturn(identityProviderResource);
  }

  @Test
  void listMappings_existingProvider_returnCorrectMappingAggregates() {
    //given
    given(identityProviderResource.getMappers()).willReturn(availableMappersFixture());

    //when
    var actual = underTest.listMappings(ID_PROVIDER_NAME);

    //then
    assertThat(actual.size()).isEqualTo(3);
    //and
    RoleMappingDto dto01 = actual.get(0);
    RoleMappingDto dto02 = actual.get(1);
    //and
    assertEquals("AnotherMapper", dto01.getName());
    assertEquals("unknown role", dto01.getRolesDto().getRoles().get(0));
    assertThat(dto01.getSsoAttributes().size()).isEqualTo(1);
    assertThat(dto01.getSsoAttributes()).contains(
        SsoAttributeDto.builder().key("k2").value("v2").build());
    //and
    assertEquals("Mapper", dto02.getName());
    assertEquals("governance.uma_protection", dto02.getRolesDto().getRoles().get(0));
    assertEquals("create policy", dto02.getRolesDto().getRoles().get(1));
    assertThat(dto02.getSsoAttributes().size()).isEqualTo(2);
    assertThat(dto02.getSsoAttributes()).contains(
        SsoAttributeDto.builder().key("k1").value("v1").build());
    assertThat(dto02.getSsoAttributes()).contains(
        SsoAttributeDto.builder().key("k3").value("v3").build());
  }

  @Test
  void listMappings_nonExistingProvider_throwsException() {
    //given
    var providerName = "Unknown identity provider";
    given(identityProvidersResource.get(providerName)).willReturn(null);

    //when
    Executable when = () -> underTest.listMappings(providerName);

    //then
    assertThrows(IdentityProviderNotFoundException.class, when);
  }

  @Test
  void addMapping_createsMultipleEntriesWithExpectedConfiguration() {
    //given
    RoleMappingDto roleMappingDto = RoleMappingDto.builder()
        .name("mapping name")
        .providerAlias(ID_PROVIDER_NAME)
        .ssoAttributes(ImmutableSet.of(
            SsoAttributeDto.builder().key("saml_attr_01").value("saml_attr_val_01").build(),
            SsoAttributeDto.builder().key("saml_attr_02").value("saml_attr_val_02").build()
        ))
        .rolesDto(new RolesDto(List.of("governance.uma_protection", "create policy")))
        .build();

    //when
    underTest.addMapping(roleMappingDto);

    //then
    verify(identityProviderResource, times(2))
        .addMapper(mapperCaptor.capture());

    assertThat(mapperCaptor.getAllValues().get(0))
        .satisfies(mapper -> {
          assertThat(mapper.getName()).contains(roleMappingDto.getName() + "_UUID_");
          assertThat(mapper.getName().length()).isEqualTo(
              (roleMappingDto.getName() + "_UUID_" + UUID.randomUUID()).length());
          assertThat(mapper.getIdentityProviderMapper()).isEqualTo("saml-advanced-role-idp-mapper");
          assertThat(mapper.getConfig().get("syncMode")).isEqualTo("FORCE");
          assertThat(mapper.getConfig().get("role"))
              .isEqualTo(roleMappingDto.getRolesDto().getRoles().get(0));
          assertThat(mapper.getConfig().get("attributes"))
              .contains("{\"key\":\"saml_attr_01\",\"value\":\"saml_attr_val_01\"}");
          assertThat(mapper.getConfig().get("attributes"))
              .contains("{\"key\":\"saml_attr_02\",\"value\":\"saml_attr_val_02\"}");
        });
    //and
    assertThat(mapperCaptor.getAllValues().get(1))
        .satisfies(mapper -> {
          assertThat(mapper.getName()).contains(roleMappingDto.getName() + "_UUID_");
          assertThat(mapper.getIdentityProviderMapper()).isEqualTo("saml-advanced-role-idp-mapper");
          assertThat(mapper.getConfig().get("syncMode")).isEqualTo("FORCE");
          assertThat(mapper.getConfig().get("role"))
              .isEqualTo(roleMappingDto.getRolesDto().getRoles().get(1));
          assertThat(mapper.getConfig().get("attributes"))
              .contains("{\"key\":\"saml_attr_01\",\"value\":\"saml_attr_val_01\"}");
          assertThat(mapper.getConfig().get("attributes"))
              .contains("{\"key\":\"saml_attr_02\",\"value\":\"saml_attr_val_02\"}");
        });
  }

  @Test
  void addMapping_mapperWithSameNameExists_throwsException() {
    //given
    String mapperName =  "Existing-Mapper";
    IdentityProviderMapperRepresentation mapper = new IdentityProviderMapperRepresentation();
    mapper.setName(mapperName + "_UUID_" + UUID.randomUUID());
    //and
    IdentityProviderRepresentation idpRep = mock(IdentityProviderRepresentation.class);
    when(idpRep.getAlias()).thenReturn(ID_PROVIDER_NAME);
    given(identityProvidersResource.findAll()).willReturn(of(idpRep));
    given(identityProviderResource.getMappers()).willReturn(of(mapper));
    RoleMappingDto roleMappingDto = RoleMappingDto.builder()
        .name(mapperName).build();

    //when
    Executable when = () -> underTest.addMapping(roleMappingDto);

    //then
    assertThrows(SsoRoleMapperAlreadyExistException.class, when);
  }

  @Test
  void deleteSsoRoleMapping() {
    given(identityProviderResource.getMappers()).willReturn(availableMappersFixture());
    mockDefaultIdentityProvider();

    //when
    underTest.deleteMapping("Mapper");

    //then
    verify(identityProviderResource, times(2))
        .delete(mapperIdCaptor.capture());
    assertThat(mapperIdCaptor.getAllValues().get(0)).isEqualTo("01");
    assertThat(mapperIdCaptor.getAllValues().get(1)).isEqualTo("03");
  }

  private void mockDefaultIdentityProvider() {
    IdentityProviderRepresentation idpRep = mock(IdentityProviderRepresentation.class);
    when(idpRep.getAlias()).thenReturn(ID_PROVIDER_NAME);
    given(identityProvidersResource.findAll()).willReturn(of(idpRep));
  }

  @Test
  void getMapping_mapperExists_returnsMapper() {
    //given
    String mappingName = "Mapper";
    given(identityProviderResource.getMappers()).willReturn(availableMappersFixture());
    mockDefaultIdentityProvider();

    //when
    RoleMappingDto mappingDto = underTest.getMapping(mappingName);

    //then
    assertThat(mappingDto.getName()).isEqualTo(mappingName);
  }

  @Test
  void getMapping_mapperNotExist_throwsException() {
    //given
    String mappingName = "not existing mapping";
    mockDefaultIdentityProvider();

    //when
    Executable when = () ->  underTest.getMapping(mappingName);

    //then
    assertThrows(SsoRoleMapperNotFoundException.class, when);
  }

  private static List<IdentityProviderMapperRepresentation> availableMappersFixture() {
    IdentityProviderMapperRepresentation mapper01 = new IdentityProviderMapperRepresentation();
    mapper01.setId("01");
    mapper01.setName("Mapper_UUID_UUID_01");
    mapper01.setIdentityProviderAlias(ID_PROVIDER_NAME);
    mapper01.setConfig(ImmutableMap.of(
        "syncMode", "FORCE",
        "attributes", "[{\"key\":\"k1\",\"value\":\"v1\"}]",
        "role", "governance.uma_protection"));

    IdentityProviderMapperRepresentation mapper02 = new IdentityProviderMapperRepresentation();
    mapper02.setId("02");
    mapper02.setName("AnotherMapper_UUID_UUID_02");
    mapper02.setIdentityProviderAlias(ID_PROVIDER_NAME);
    mapper02.setConfig(ImmutableMap.of(
        "syncMode", "FORCE",
        "attributes", "[{\"key\":\"k2\",\"value\":\"v2\"}]",
        "role", "unknown role"));

    IdentityProviderMapperRepresentation mapper03 = new IdentityProviderMapperRepresentation();
    mapper03.setId("03");
    mapper03.setName("Mapper_UUID_UUID_03");
    mapper03.setIdentityProviderAlias(ID_PROVIDER_NAME);
    mapper03.setConfig(ImmutableMap.of(
        "syncMode", "FORCE",
        "attributes", "[{\"key\":\"k3\",\"value\":\"v3\"}]",
        "role", "create policy"));

    IdentityProviderMapperRepresentation mapper04 = new IdentityProviderMapperRepresentation();
    mapper04.setId("04");
    mapper04.setName("MapperMapperMapper_UUID_UUID_04");
    mapper04.setIdentityProviderAlias(ID_PROVIDER_NAME);
    mapper04.setConfig(ImmutableMap.of(
        "syncMode", "FORCE",
        "attributes", "[{\"key\":\"k4\",\"value\":\"v4\"}]",
        "role", "create policy"));

    return of(mapper01, mapper02, mapper03, mapper04);
  }
}