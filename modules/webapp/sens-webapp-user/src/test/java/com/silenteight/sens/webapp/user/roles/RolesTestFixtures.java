package com.silenteight.sens.webapp.user.roles;

import com.silenteight.sens.webapp.user.roles.create.dto.CreateRoleDto;
import com.silenteight.sens.webapp.user.roles.edit.dto.EditRoleDto;
import com.silenteight.sens.webapp.user.roles.list.dto.PermissionDto;
import com.silenteight.sens.webapp.user.roles.list.dto.RoleDto;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static com.silenteight.sens.webapp.user.roles.domain.RoleState.IN_USE;
import static java.time.OffsetDateTime.parse;
import static java.util.UUID.fromString;
import static java.util.UUID.randomUUID;

public class RolesTestFixtures {

  public static final UUID ROLE_ID = fromString("bc02f5af-9ea7-4505-839f-90a6160caa25");
  public static final UUID PERMISSION_ID_1 = fromString("ec75b706-43e9-49ae-b328-0ef372f07058");
  public static final UUID PERMISSION_ID_2 = fromString("a6e9de43-6041-49eb-8eb6-b9ecaa99bf50");
  public static final OffsetDateTime TIMESTAMP_CREATED_AT = parse("2021-07-22T12:20:37.098Z");
  public static final OffsetDateTime TIMESTAMP_UPDATED_AT = parse("2021-07-22T12:20:37.098Z");
  public static final String ROLE_NAME_1 = "First role name";
  public static final String ROLE_NAME_2 = "Second role name ";
  public static final String ROLE_DESCRIPTION_1 = "First tole description";
  public static final String ROLE_DESCRIPTION_2 = "Second tole description";
  public static final String USER_NAME_1 = "John Doe";
  public static final String USER_NAME_2 = "Jane Doe";
  public static final String PERMISSION_GOVERNANCE_GROUP = "Governance";
  public static final String PERMISSION_WAREHOUSE_GROUP = "Warehouse";
  public static final String PERMISSION_NAME_1 = "IMPORT_POLICY";
  public static final String PERMISSION_DESCRIPTION_1 =
      "Permission that allows use to import policy.";

  public static final String PERMISSION_NAME_2 = "VIEW_QA_ALERTS";
  public static final String PERMISSION_DESCRIPTION_2 =
      "Permission that allows user to se QA alerts";

  public static final CreateRoleDto CREATE_ROLE_DTO = CreateRoleDto.builder()
      .id(randomUUID())
      .name("Role name")
      .description("Role description")
      .build();

  public static final RoleDto ROLE_DTO_1 = RoleDto.builder()
      .id(ROLE_ID)
      .name(ROLE_NAME_1)
      .description(ROLE_DESCRIPTION_1)
      .state(IN_USE)
      .createdAt(TIMESTAMP_CREATED_AT)
      .updatedAt(TIMESTAMP_UPDATED_AT)
      .createdBy(USER_NAME_1)
      .build();

  public static final RoleDto ROLE_DTO_2 = RoleDto.builder()
      .id(ROLE_ID)
      .name(ROLE_NAME_2)
      .description(ROLE_DESCRIPTION_2)
      .state(IN_USE)
      .createdAt(TIMESTAMP_CREATED_AT)
      .updatedAt(TIMESTAMP_UPDATED_AT)
      .createdBy(USER_NAME_2)
      .build();

  public static final PermissionDto PERMISSION_DTO_1 = PermissionDto.builder()
      .id(PERMISSION_ID_1)
      .group(PERMISSION_GOVERNANCE_GROUP)
      .name(PERMISSION_NAME_1)
      .description(PERMISSION_DESCRIPTION_1)
      .build();

  public static final PermissionDto PERMISSION_DTO_2 = PermissionDto.builder()
      .id(PERMISSION_ID_2)
      .group(PERMISSION_WAREHOUSE_GROUP)
      .name(PERMISSION_NAME_2)
      .description(PERMISSION_DESCRIPTION_2)
      .build();

  public static final EditRoleDto EDIT_ROLE_DTO = EditRoleDto.builder()
      .id(ROLE_ID)
      .name("Role new name")
      .description("Role new description")
      .build();

  public static final List<UUID> PERMISSIONS = List.of(PERMISSION_ID_1, PERMISSION_ID_2);

  public static final List<RoleDto> ROLE_DTOS_LIST = List.of(ROLE_DTO_1, ROLE_DTO_2);

  public static final List<PermissionDto> PERMISSIONS_DTOS_LIST =
      List.of(PERMISSION_DTO_1, PERMISSION_DTO_2);
}
