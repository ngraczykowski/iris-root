package com.silenteight.sens.webapp.sso;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.sens.webapp.sso.create.CreateSsoMappingCommand;
import com.silenteight.sens.webapp.sso.create.dto.CreateSsoMappingDto;
import com.silenteight.sens.webapp.sso.list.dto.AttributeToRoleDto;
import com.silenteight.sens.webapp.sso.list.dto.SsoMappingDto;
import com.silenteight.sep.usermanagement.api.identityprovider.dto.RoleMappingDto;
import com.silenteight.sep.usermanagement.api.identityprovider.dto.SsoAttributeDto;
import com.silenteight.sep.usermanagement.api.role.dto.RolesDto;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.silenteight.sens.webapp.common.testing.rest.TestRoles.AUDITOR;
import static com.silenteight.sens.webapp.common.testing.rest.TestRoles.USER_ADMINISTRATOR;
import static com.silenteight.sens.webapp.sso.domain.DomainConstants.SSO_MAPPING_FIELD_MAX_LENGTH;
import static java.util.Collections.emptyList;
import static java.util.List.of;
import static java.util.UUID.fromString;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SsoMappingTestFixtures {

  public static final String SS0_NAME = "Sso name 1";
  public static final String SS0_NAME_2 = "Sso name 2";
  public static final String SS0_NAME_WITH_LESS_CHARS_THEN_REQUIRED = "TN";
  public static final String SS0_NAME_WITH_MORE_CHARS_THEN_PERMITTED =
      "s".repeat(SSO_MAPPING_FIELD_MAX_LENGTH + 1);

  public static final String SSO_ATTRIBUTE_1 = "Attribute #1";
  public static final String SSO_ATTRIBUTE_2 = "Attribute #2";
  public static final String SSO_ATTRIBUTE_NAME_TOO_SHORT = "At";
  public static final String SSO_ATTRIBUTE_NAME_TOO_LONG =
      "s".repeat(SSO_MAPPING_FIELD_MAX_LENGTH + 20);

  public static final String SSO_ROLE_1 = "Role #1";
  public static final String SSO_ROLE_2 = "Role #2";
  public static final String SSO_ROLE_NAME_TOO_SHORT = "Ro";
  public static final String SSO_ROLE_NAME_TOO_LONG =
      "s".repeat(SSO_MAPPING_FIELD_MAX_LENGTH + 1);

  public static final List<String> ROLES_NAMES_LIST = of(USER_ADMINISTRATOR, AUDITOR);
  public static final RolesDto ROLES_DTO = new RolesDto(ROLES_NAMES_LIST);
  public static final UUID SSO_ID_1 = fromString("e0c46896-6448-4b23-8473-1be5a40f08fb");
  public static final UUID SSO_ID_2 = fromString("c00c5c24-ddaa-46aa-8d65-15da53b3a295");
  public static final UUID SSO_ID_3 = fromString("41070168-1dab-432d-81ee-37ff7730c22e");
  public static final List<String> ROLES_LIST = of(USER_ADMINISTRATOR, AUDITOR);

  public static final SsoAttributeDto SSO_ATTRIBUTE_DTO_1 = SsoAttributeDto.builder()
      .key(SSO_ATTRIBUTE_1)
      .value(SSO_ROLE_1)
      .build();

  public static final SsoAttributeDto SSO_ATTRIBUTE_DTO_2 = SsoAttributeDto.builder()
      .key(SSO_ATTRIBUTE_2)
      .value(SSO_ROLE_2)
      .build();

  public static final AttributeToRoleDto ATTRIBUTE_TO_ROLE_DTO_1 = AttributeToRoleDto.builder()
      .attribute(SSO_ATTRIBUTE_1)
      .role(SSO_ROLE_1)
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
      of(ROLE_MAPPING_DTO_1, ROLE_MAPPING_DTO_2, ROLE_MAPPING_DTO_3);

  public static final AttributeToRoleDto ATTRIBUTE_TO_ROLE_DTO = AttributeToRoleDto.builder()
      .attribute(SSO_ATTRIBUTE_1)
      .role(SSO_ROLE_1)
      .build();

  public static final AttributeToRoleDto ATTRIBUTE_TO_ROLE_DTO_2 = AttributeToRoleDto.builder()
      .attribute(SSO_ATTRIBUTE_2)
      .role(SSO_ROLE_2)
      .build();

  public static final AttributeToRoleDto ATTRIBUTE_TO_ROLE_DTO_WITH_ATTRIBUTE_NAME_TOO_LONG =
      AttributeToRoleDto.builder()
          .attribute(SSO_ATTRIBUTE_NAME_TOO_LONG)
          .role(SSO_ROLE_2)
          .build();

  public static final AttributeToRoleDto ATTRIBUTE_TO_ROLE_DTO_WITH_ATTRIBUTE_NAME_TOO_SHORT =
      AttributeToRoleDto.builder()
          .attribute(SSO_ATTRIBUTE_NAME_TOO_SHORT)
          .role(SSO_ROLE_2)
          .build();

  public static final AttributeToRoleDto ATTRIBUTE_TO_ROLE_DTO_WITH_ROLE_NAME_TOO_LONG =
      AttributeToRoleDto.builder()
          .attribute(SSO_ATTRIBUTE_2)
          .role(SSO_ROLE_NAME_TOO_LONG)
          .build();

  public static final AttributeToRoleDto ATTRIBUTE_TO_ROLE_DTO_WITH_ROLE_NAME_TOO_SHORT =
      AttributeToRoleDto.builder()
          .attribute(SSO_ATTRIBUTE_2)
          .role(SSO_ROLE_NAME_TOO_SHORT)
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
      of(SSO_MAPPING_DTO_1, SSO_MAPPING_DTO_2);

  public static final CreateSsoMappingCommand CREATE_SSO_MAPPING_COMMAND =
      CreateSsoMappingCommand.builder()
          .name(SS0_NAME)
          .attributes(of(ATTRIBUTE_TO_ROLE_DTO_1))
          .roles(ROLES_LIST)
          .build();

  public static final CreateSsoMappingDto CREATE_SSO_MAPPING_DTO =
      new CreateSsoMappingDto(SS0_NAME, of(), of(USER_ADMINISTRATOR, AUDITOR));

  public static final CreateSsoMappingDto CREATE_SSO_MAPPING_DTO_WITH_TOO_LONG_NAME =
      new CreateSsoMappingDto(
          SS0_NAME_WITH_MORE_CHARS_THEN_PERMITTED, of(), of(USER_ADMINISTRATOR, AUDITOR));

  public static final CreateSsoMappingDto CREATE_SSO_MAPPING_DTO_WITH_TOO_SHORT_NAME =
      new CreateSsoMappingDto(
          SS0_NAME_WITH_LESS_CHARS_THEN_REQUIRED, of(), of(USER_ADMINISTRATOR, AUDITOR));

  public static final CreateSsoMappingDto CREATE_SSO_MAPPING_DTO_WITH_ATTRIBUTE_NAME_TOO_LONG =
      new CreateSsoMappingDto(
          SS0_NAME, of(ATTRIBUTE_TO_ROLE_DTO_WITH_ATTRIBUTE_NAME_TOO_LONG),
          of(USER_ADMINISTRATOR, AUDITOR));

  public static final CreateSsoMappingDto CREATE_SSO_MAPPING_DTO_WITH_ATTRIBUTE_NAME_TOO_SHORT =
      new CreateSsoMappingDto(
          SS0_NAME, of(ATTRIBUTE_TO_ROLE_DTO_WITH_ATTRIBUTE_NAME_TOO_SHORT),
          of(USER_ADMINISTRATOR, AUDITOR));

  public static final CreateSsoMappingDto CREATE_SSO_MAPPING_DTO_WITH_ROLE_NAME_TOO_LONG =
      new CreateSsoMappingDto(
          SS0_NAME, of(ATTRIBUTE_TO_ROLE_DTO_WITH_ROLE_NAME_TOO_LONG),
          of(USER_ADMINISTRATOR, AUDITOR));

  public static final CreateSsoMappingDto CREATE_SSO_MAPPING_DTO_WITH_ROLE_NAME_TOO_SHORT =
      new CreateSsoMappingDto(
          SS0_NAME, of(ATTRIBUTE_TO_ROLE_DTO_WITH_ROLE_NAME_TOO_SHORT),
          of(USER_ADMINISTRATOR, AUDITOR));

}
