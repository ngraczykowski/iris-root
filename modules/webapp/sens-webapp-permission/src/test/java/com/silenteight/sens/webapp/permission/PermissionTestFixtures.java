package com.silenteight.sens.webapp.permission;

import com.silenteight.sens.webapp.permission.list.dto.PermissionDto;

import java.util.List;
import java.util.UUID;

import static java.util.UUID.fromString;

public class PermissionTestFixtures {

  public static final UUID ROLE_ID = fromString("bc02f5af-9ea7-4505-839f-90a6160caa25");
  public static final UUID PERMISSION_ID_1 = fromString("ec75b706-43e9-49ae-b328-0ef372f07058");
  public static final String PERMISSION_NAME_1 = "IMPORT_POLICY";
  public static final String PERMISSION_DESCRIPTION_1 =
      "Permission that allows use to import policy.";
  public static final UUID PERMISSION_ID_2 = fromString("a6e9de43-6041-49eb-8eb6-b9ecaa99bf50");
  public static final String PERMISSION_NAME_2 = "VIEW_QA_ALERTS";
  public static final String PERMISSION_DESCRIPTION_2 =
      "Permission that allows user to see QA alerts";
  public static final String USERNAME_1 = "John Doe";
  public static final String USERNAME_2 = "Adam Smith";

  public static final PermissionDto PERMISSION_DTO_1 = PermissionDto.builder()
      .id(PERMISSION_ID_1)
      .name(PERMISSION_NAME_1)
      .description(PERMISSION_DESCRIPTION_1)
      .build();

  public static final PermissionDto PERMISSION_DTO_2 = PermissionDto.builder()
      .id(PERMISSION_ID_2)
      .name(PERMISSION_NAME_2)
      .description(PERMISSION_DESCRIPTION_2)
      .build();

  public static final List<PermissionDto> PERMISSIONS_DTOS_LIST =
      List.of(PERMISSION_DTO_1, PERMISSION_DTO_2);
}
