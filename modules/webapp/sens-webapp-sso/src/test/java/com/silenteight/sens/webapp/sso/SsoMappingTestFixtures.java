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

import static java.util.Collections.emptyList;

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

  public static final SsoAttributeDto SSO_ATTRIBUTE_DTO_1 = SsoAttributeDto.builder()
      .key(SSO_ATTRIBUTE_KEY_1)
      .value(SSO_ATTRIBUTE_VALUE_1)
      .build();

  public static final SsoAttributeDto SSO_ATTRIBUTE_DTO_2 = SsoAttributeDto.builder()
      .key(SSO_ATTRIBUTE_KEY_2)
      .value(SSO_ATTRIBUTE_VALUE_2)
      .build();

  public static final RoleMappingDto ROLE_MAPPING_DTO_1 = RoleMappingDto.builder()
      .name(SS0_NAME)
      .ssoAttributes(Set.of(SSO_ATTRIBUTE_DTO_1))
      .rolesDto(ROLES_DTO)
      .build();

  public static final RoleMappingDto ROLE_MAPPING_DTO_2 = RoleMappingDto.builder()
      .name("Role mapping name #2")
      .rolesDto(ROLES_DTO)
      .build();

  public static final RoleMappingDto ROLE_MAPPING_DTO_3 = RoleMappingDto.builder()
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
      .name(SS0_NAME)
      .attributeToRoleDtoSet(Set.of(ATTRIBUTE_TO_ROLE_DTO))
      .roles(ROLES_NAMES_LIST)
      .build();

  public static final SsoMappingDto SSO_MAPPING_DTO_2 = SsoMappingDto.builder()
      .name(SS0_NAME_2)
      .attributeToRoleDtoSet(Set.of(ATTRIBUTE_TO_ROLE_DTO_2))
      .roles(emptyList())
      .build();

  public static final List<SsoMappingDto> SSO_MAPPING_DTO_LIST =
      List.of(SSO_MAPPING_DTO_1, SSO_MAPPING_DTO_2);
}
