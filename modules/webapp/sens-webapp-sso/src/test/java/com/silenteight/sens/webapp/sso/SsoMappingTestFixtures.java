package com.silenteight.sens.webapp.sso;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.sens.webapp.sso.list.dto.AttributeToRoleDto;
import com.silenteight.sens.webapp.sso.list.dto.SsoMappingDto;
import com.silenteight.sep.usermanagement.api.dto.RoleMappingDto;
import com.silenteight.sep.usermanagement.api.dto.RolesDto;
import com.silenteight.sep.usermanagement.api.dto.SsoAttributeDto;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static java.util.Collections.emptyList;
import static java.util.UUID.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SsoMappingTestFixtures {

  public static final String SS0_NAME = "Sso name 1";
  public static final String SS0_NAME_2 = "Sso name 2";
  public static final String ROLE_NAME_1 = "Role name #1";
  public static final String ROLE_NAME_2 = "Role name #1";
  public static final String SSO_ATTRIBUTE_KEY_1 = "Key #1";
  public static final String SSO_ATTRIBUTE_KEY_2 = "Key #2";
  public static final String SSO_ATTRIBUTE_VALUE_1 = "Value #1";
  public static final String SSO_ATTRIBUTE_VALUE_2 = "Value #2";
  public static final List<String> ROLES_NAMES_LIST = List.of(ROLE_NAME_1, ROLE_NAME_2);
  public static final RolesDto ROLES_DTO = new RolesDto(ROLES_NAMES_LIST);
  public static final UUID SSO_ID_1 = fromString("e0c46896-6448-4b23-8473-1be5a40f08fb");
  public static final UUID SSO_ID_2 = fromString("c00c5c24-ddaa-46aa-8d65-15da53b3a295");
  public static final UUID SSO_ID_3 = fromString("41070168-1dab-432d-81ee-37ff7730c22e");
  public static final SsoAttributeDto SSO_ATTRIBUTE_DTO_1 = SsoAttributeDto.builder()
      .key(SSO_ATTRIBUTE_KEY_1)
      .value(SSO_ATTRIBUTE_VALUE_1)
      .build();

  public static final SsoAttributeDto SSO_ATTRIBUTE_DTO_2 = SsoAttributeDto.builder()
      .key(SSO_ATTRIBUTE_KEY_2)
      .value(SSO_ATTRIBUTE_VALUE_2)
      .build();

  public static final RoleMappingDto ROLE_MAPPING_DTO_1 = RoleMappingDto.builder()
      .id(SSO_ID_1)
      .name(SS0_NAME)
      .ssoAttributes(Set.of(SSO_ATTRIBUTE_DTO_1))
      .rolesDto(ROLES_DTO)
      .build();

  public static final RoleMappingDto ROLE_MAPPING_DTO_2 = RoleMappingDto.builder()
      .id(SSO_ID_2)
      .name("Role mapping name #2")
      .rolesDto(ROLES_DTO)
      .build();

  public static final RoleMappingDto ROLE_MAPPING_DTO_3 = RoleMappingDto.builder()
      .id(SSO_ID_3)
      .name("Role mapping name #3")
      .ssoAttributes(Set.of(SSO_ATTRIBUTE_DTO_1, SSO_ATTRIBUTE_DTO_2))
      .rolesDto(ROLES_DTO)
      .build();

  public static final List<RoleMappingDto> ROLE_MAPPING_DTO_LIST =
      List.of(ROLE_MAPPING_DTO_1, ROLE_MAPPING_DTO_2, ROLE_MAPPING_DTO_3);

  public static final AttributeToRoleDto ATTRIBUTE_TO_ROLE_DTO = AttributeToRoleDto.builder()
      .key(SSO_ATTRIBUTE_KEY_1)
      .value(SSO_ATTRIBUTE_VALUE_1)
      .build();

  public static final AttributeToRoleDto ATTRIBUTE_TO_ROLE_DTO_2 = AttributeToRoleDto.builder()
      .key(SSO_ATTRIBUTE_KEY_2)
      .value(SSO_ATTRIBUTE_VALUE_2)
      .build();

  public static final SsoMappingDto SSO_MAPPING_DTO_1 = SsoMappingDto.builder()
      .id(SSO_ID_1)
      .name(SS0_NAME)
      .attributeToRoleDtoSet(Set.of(ATTRIBUTE_TO_ROLE_DTO))
      .roles(ROLES_NAMES_LIST)
      .build();

  public static final SsoMappingDto SSO_MAPPING_DTO_2 = SsoMappingDto.builder()
      .id(SSO_ID_2)
      .name(SS0_NAME_2)
      .attributeToRoleDtoSet(Set.of(ATTRIBUTE_TO_ROLE_DTO_2))
      .roles(emptyList())
      .build();

  public static final List<SsoMappingDto> SSO_MAPPING_DTO_LIST =
      List.of(SSO_MAPPING_DTO_1, SSO_MAPPING_DTO_2);
}
