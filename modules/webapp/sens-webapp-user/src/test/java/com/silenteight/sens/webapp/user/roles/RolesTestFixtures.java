package com.silenteight.sens.webapp.user.roles;

import com.silenteight.sens.webapp.user.roles.create.dto.CreateRoleDto;
import com.silenteight.sens.webapp.user.roles.edit.dto.EditRoleDto;

import java.util.List;
import java.util.UUID;

import static java.util.UUID.fromString;
import static java.util.UUID.randomUUID;

public class RolesTestFixtures {

  public static final UUID ROLE_ID = fromString("bc02f5af-9ea7-4505-839f-90a6160caa25");
  public static final UUID PERMISSION_ID_1 = fromString("ec75b706-43e9-49ae-b328-0ef372f07058");
  public static final UUID PERMISSION_ID_2 = fromString("a6e9de43-6041-49eb-8eb6-b9ecaa99bf50");

  public static final CreateRoleDto CREATE_ROLE_DTO = CreateRoleDto.builder()
      .id(randomUUID())
      .name("Role name")
      .description("Role description")
      .build();

  public static final EditRoleDto EDIT_ROLE_DTO = EditRoleDto.builder()
      .id(ROLE_ID)
      .name("Role new name")
      .description("Role new description")
      .build();

  public static final List<UUID> PERMISSIONS = List.of(PERMISSION_ID_1, PERMISSION_ID_2);
}
