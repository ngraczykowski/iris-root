package com.silenteight.sep.usermanagement.keycloak.sso;

import com.silenteight.sep.usermanagement.api.identityprovider.dto.CreateRoleMappingDto;
import com.silenteight.sep.usermanagement.api.identityprovider.dto.RoleMappingDto;
import com.silenteight.sep.usermanagement.api.identityprovider.dto.SsoAttributeDto;
import com.silenteight.sep.usermanagement.api.identityprovider.exception.SsoRoleMapperAlreadyExistsException;
import com.silenteight.sep.usermanagement.api.role.dto.RolesDto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.keycloak.admin.client.resource.IdentityProviderResource;
import org.keycloak.admin.client.resource.IdentityProvidersResource;
import org.keycloak.representations.idm.IdentityProviderMapperRepresentation;
import org.keycloak.representations.idm.IdentityProviderRepresentation;
import org.mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;
import java.util.UUID;
import javax.ws.rs.NotFoundException;

import static java.util.List.of;
import static java.util.UUID.fromString;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class KeycloakSsoRoleMapperTest {

  public static final String SAML_ATTR_KEY_VALUE_1 =
      "{\"key\":\"saml_attr_01\",\"value\":\"saml_attr_val_01\"}";
  public static final String SAML_ATTR_KEY_VALUE_2 =
      "{\"key\":\"saml_attr_02\",\"value\":\"saml_attr_val_02\"}";
  @Mock
  private IdentityProvidersResource identityProvidersResource;

  @Mock
  private IdentityProviderResource identityProviderResource;

  private KeycloakSsoRoleMapper underTest;

  private static final String ID_PROVIDER_NAME = "Saml Identity Provider";
  private static final String EXISTING_MAPPER_ID = "945913ed-a134-4803-a4b0-c8b1e12337c4";
  private static final String LEGACY_MAPPING_UUID = "c904fbee-1ca5-4228-83b1-fe91e636804f";

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
    mockDefaultIdentityProvider();
    CreateRoleMappingDto roleMappingDto = CreateRoleMappingDto.builder()
        .name("Mapping name")
        .ssoAttributes(ImmutableSet.of(
            SsoAttributeDto.builder().key("saml_attr_01").value("saml_attr_val_01").build(),
            SsoAttributeDto.builder().key("saml_attr_02").value("saml_attr_val_02").build()
        ))
        .roles(new RolesDto(List.of("governance.uma_protection", "create policy")))
        .build();
    RoleMappingDto result;
    try (
        MockedStatic<KeycloakMapperFinder> keycloakMapperFinder = mockStatic(
            KeycloakMapperFinder.class)) {
      keycloakMapperFinder
          .when(() -> KeycloakMapperFinder.findKeycloakMappersBySharedId(any(), any()))
          .thenAnswer(this::getIdentityProviderMapperRepresentations);
      //when
      result = underTest.addMapping(roleMappingDto);
    }
    //then
    result.getName();
    verify(identityProviderResource, times(2))
        .addMapper(mapperCaptor.capture());
    assertThat(mapperCaptor.getAllValues().get(0))
        .satisfies(mapper -> {
          String mapperName = mapper.getName();
          assertThat(mapperName).matches("Mapping name.*_ID_.*_SUB_\\d+");
          assertThat(mapperName.length()).isEqualTo((roleMappingDto.getName() +
              "_ID_" + UUID.randomUUID() + "_SUB_0").length());
          assertThat(mapper.getIdentityProviderMapper()).isEqualTo("saml-advanced-role-idp-mapper");
          assertThat(mapper.getConfig().get("syncMode")).isEqualTo("FORCE");
          assertThat(mapper.getConfig().get("role"))
              .isEqualTo(roleMappingDto.getRoles().getRoles().get(0));
          assertThat(mapper.getConfig().get("attributes")).contains(SAML_ATTR_KEY_VALUE_1);
          assertThat(mapper.getConfig().get("attributes")).contains(SAML_ATTR_KEY_VALUE_2);
        });
    //and
    assertThat(mapperCaptor.getAllValues().get(1))
        .satisfies(mapper -> {
          String mapperName = mapper.getName();
          assertThat(mapperName).matches("Mapping name.*_ID_.*_SUB_\\d+");
          assertThat(mapperName.length()).isEqualTo((roleMappingDto.getName() +
              "_ID_" + UUID.randomUUID() + "_SUB_0").length());
          assertThat(mapper.getIdentityProviderMapper()).isEqualTo("saml-advanced-role-idp-mapper");
          assertThat(mapper.getConfig().get("syncMode")).isEqualTo("FORCE");
          assertThat(mapper.getConfig().get("role"))
              .isEqualTo(roleMappingDto.getRoles().getRoles().get(1));
          assertThat(mapper.getConfig().get("attributes")).contains(SAML_ATTR_KEY_VALUE_1);
          assertThat(mapper.getConfig().get("attributes")).contains(SAML_ATTR_KEY_VALUE_2);
        });
  }

  @Test
  void addMapping_mapperWithSameNameExists_throwsException() {
    //given
    String mapperName = "Existing-Mapper";
    UUID existingMappingID = fromString(EXISTING_MAPPER_ID);
    IdentityProviderMapperRepresentation mapper = new IdentityProviderMapperRepresentation();
    mapper.setName(mapperName + "_ID_" + existingMappingID + "_SUB_20");
    //and
    IdentityProviderRepresentation idpRep = mock(IdentityProviderRepresentation.class);
    when(idpRep.getAlias()).thenReturn(ID_PROVIDER_NAME);
    given(identityProvidersResource.findAll()).willReturn(of(idpRep));
    given(identityProviderResource.getMappers()).willReturn(of(mapper));
    CreateRoleMappingDto roleMappingDto = CreateRoleMappingDto.builder()
        .name(mapperName).build();

    //when
    Executable when = () -> underTest.addMapping(roleMappingDto);

    //then
    assertThrows(SsoRoleMapperAlreadyExistsException.class, when);
  }

  @Test
  void deleteSsoRoleMapping() {
    //given
    UUID existingMappingID = fromString(EXISTING_MAPPER_ID);
    given(identityProviderResource.getMappers()).willReturn(availableMappersFixture());
    given(identityProviderResource.getMapperById(EXISTING_MAPPER_ID))
        .willThrow(NotFoundException.class);
    mockDefaultIdentityProvider();

    //when
    underTest.deleteMapping(existingMappingID);

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
    given(identityProviderResource.toRepresentation()).willReturn(idpRep);
    given(identityProvidersResource.get(ID_PROVIDER_NAME)).willReturn(identityProviderResource);
  }

  @Test
  void getMapping_mapperExists_returnsMapper() {
    //given
    UUID existingMappingId = fromString(EXISTING_MAPPER_ID);
    given(identityProviderResource.getMappers()).willReturn(availableMappersFixture());
    given(identityProviderResource.getMapperById(EXISTING_MAPPER_ID))
        .willThrow(NotFoundException.class);
    mockDefaultIdentityProvider();

    //when
    RoleMappingDto mappingDto = underTest.getMapping(existingMappingId);

    //then
    assertThat(mappingDto.getId()).isEqualTo(existingMappingId);
    assertThat(mappingDto.getName()).isEqualTo("Mapper");
  }

  @Test
  void getMapping_mapperNotExist_throwsException() {
    //given
    mockDefaultIdentityProvider();
    UUID notExistingMapperID = UUID.randomUUID();
    given(identityProviderResource.getMapperById(notExistingMapperID.toString()))
        .willThrow(NotFoundException.class);

    //when
    Executable when = () -> underTest.getMapping(notExistingMapperID);

    //then
    assertThrows(SsoRoleMapperNotFoundException.class, when);
  }

  @Test
  void listMappings_returnsLegacyMappings() {
    //given
    given(identityProviderResource.getMappers()).willReturn(legacyMappersFixture());

    //when
    var legacyMappings = underTest.listMappings(ID_PROVIDER_NAME);

    //then
    assertThat(legacyMappings).isNotEmpty();
  }

  @Test
  void deleteLegacySsoRoleMapping() {
    //given
    mockDefaultIdentityProvider();
    final UUID legacyMappingID = fromString(LEGACY_MAPPING_UUID);
    given(identityProviderResource.getMapperById(LEGACY_MAPPING_UUID))
        .willReturn(legacyMappersFixture().get(0));

    //when
    underTest.deleteMapping(legacyMappingID);

    //then
    verify(identityProviderResource, times(1))
        .delete(mapperIdCaptor.capture());
    assertThat(mapperIdCaptor.getValue()).isEqualTo(LEGACY_MAPPING_UUID);
  }

  @NotNull
  private List<IdentityProviderMapperRepresentation> getIdentityProviderMapperRepresentations(
      InvocationOnMock invocation) {
    IdentityProviderMapperRepresentation mapper01 =
        new IdentityProviderMapperRepresentation();
    mapper01.setId("01");
    mapper01.setName("Mapper" + "_ID_" + invocation.getArgument(1) + "_SUB_01");
    mapper01.setIdentityProviderAlias(ID_PROVIDER_NAME);
    mapper01.setConfig(ImmutableMap.of(
        "syncMode", "FORCE",
        "attributes", "[" + SAML_ATTR_KEY_VALUE_1 + "," + SAML_ATTR_KEY_VALUE_2
            + "]",
        "role", "governance.uma_protection"));
    IdentityProviderMapperRepresentation mapper02 =
        new IdentityProviderMapperRepresentation();
    mapper01.setId("02");
    mapper02.setName("Mapper" + "_ID_" + invocation.getArgument(1) + "_SUB_02");
    mapper02.setIdentityProviderAlias(ID_PROVIDER_NAME);
    mapper02.setConfig(ImmutableMap.of(
        "syncMode", "FORCE",
        "attributes", "[" + SAML_ATTR_KEY_VALUE_1 + "," + SAML_ATTR_KEY_VALUE_2
            + "]",
        "role", "create policy"));
    return List.of(mapper01, mapper02);
  }

  private static List<IdentityProviderMapperRepresentation> availableMappersFixture() {
    IdentityProviderMapperRepresentation mapper01 = new IdentityProviderMapperRepresentation();
    mapper01.setId("01");
    mapper01.setName("Mapper" + "_ID_" + EXISTING_MAPPER_ID + "_SUB_01");
    mapper01.setIdentityProviderAlias(ID_PROVIDER_NAME);
    mapper01.setConfig(ImmutableMap.of(
        "syncMode", "FORCE",
        "attributes", "[{\"key\":\"k1\",\"value\":\"v1\"}]",
        "role", "governance.uma_protection"));

    IdentityProviderMapperRepresentation mapper02 = new IdentityProviderMapperRepresentation();
    mapper02.setId("02");
    mapper02.setName("AnotherMapper_ID_f46237ab-0f8b-4937-8249-6faf72ded214_SUB_02");
    mapper02.setIdentityProviderAlias(ID_PROVIDER_NAME);
    mapper02.setConfig(ImmutableMap.of(
        "syncMode", "FORCE",
        "attributes", "[{\"key\":\"k2\",\"value\":\"v2\"}]",
        "role", "unknown role"));

    IdentityProviderMapperRepresentation mapper03 = new IdentityProviderMapperRepresentation();
    mapper03.setId("03");
    mapper03.setName("Mapper" + "_ID_" + EXISTING_MAPPER_ID + "_SUB_03");
    mapper03.setIdentityProviderAlias(ID_PROVIDER_NAME);
    mapper03.setConfig(ImmutableMap.of(
        "syncMode", "FORCE",
        "attributes", "[{\"key\":\"k3\",\"value\":\"v3\"}]",
        "role", "create policy"));

    IdentityProviderMapperRepresentation mapper04 = new IdentityProviderMapperRepresentation();
    mapper04.setId("04");
    mapper04.setName("MapperMapperMapper_ID_1c2239ea-7b4b-4605-a4de-be209e32084c_SUB_4");
    mapper04.setIdentityProviderAlias(ID_PROVIDER_NAME);
    mapper04.setConfig(ImmutableMap.of(
        "syncMode", "FORCE",
        "attributes", "[{\"key\":\"k4\",\"value\":\"v4\"}]",
        "role", "create policy"));

    return of(mapper01, mapper02, mapper03, mapper04);
  }

  private static List<IdentityProviderMapperRepresentation> legacyMappersFixture() {
    IdentityProviderMapperRepresentation legacyMapper = new IdentityProviderMapperRepresentation();
    legacyMapper.setId(LEGACY_MAPPING_UUID);
    legacyMapper.setName("legacy mapper 01");
    legacyMapper.setIdentityProviderAlias(ID_PROVIDER_NAME);
    legacyMapper.setConfig(ImmutableMap.of(
        "syncMode", "FORCE",
        "attributes", "[{\"key\":\"k1\",\"value\":\"v1\"}]",
        "role", "governance.uma_protection"));

    return of(legacyMapper);
  }
}
