package com.silenteight.sens.webapp.role;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.sens.webapp.role.create.CreateRoleRequest;
import com.silenteight.sens.webapp.role.create.dto.CreateRoleDto;
import com.silenteight.sens.webapp.role.details.dto.RoleDetailsDto;
import com.silenteight.sens.webapp.role.list.dto.RoleDto;
import com.silenteight.sens.webapp.role.remove.RemoveRoleRequest;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.silenteight.sens.webapp.role.domain.DomainConstants.ROLE_FIELD_MAX_LENGTH;
import static java.time.OffsetDateTime.parse;
import static java.util.Set.of;
import static java.util.UUID.fromString;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RoleTestFixtures {

  public static final UUID ROLE_ID_1 = fromString("bc02f5af-9ea7-4505-839f-90a6160caa25");
  public static final UUID ROLE_ID_2 = fromString("65608792-1086-4fe8-bc80-55a351bd2018");
  public static final OffsetDateTime CREATED_AT = parse("2021-07-22T12:20:37.098Z");
  public static final OffsetDateTime UPDATED_AT = parse("2021-07-22T12:20:37.098Z");
  public static final String ROLE_NAME_1 = "First role name";
  public static final String ROLE_NAME_2 = "Second role name";
  public static final String ROLE_NAME_TOO_LONG = "a".repeat(ROLE_FIELD_MAX_LENGTH + 1);
  public static final String ROLE_NAME_WITH_LESS_THAN_MIN_NUMBER_OF_CHAR = "TR";
  public static final String ROLE_DESCRIPTION_1 = "First tole description";
  public static final String ROLE_DESCRIPTION_2 = "Second tole description";
  public static final String ROLE_DESCRIPTION_WITH_LESS_CHARS_THEN_REQUIRED = "Rd";
  public static final String ROLE_DESCRIPTION_WITH_MORE_CHARS_THEN_PERMITTED =
      "a".repeat(ROLE_FIELD_MAX_LENGTH + 1);

  public static final String USERNAME_1 = "John Doe";
  public static final UUID PERMISSION_ID_1 = fromString("ec75b706-43e9-49ae-b328-0ef372f07058");
  public static final UUID PERMISSION_ID_2 = fromString("a6e9de43-6041-49eb-8eb6-b9ecaa99bf50");
  public static final Set<UUID> PERMISSION_IDS = of(PERMISSION_ID_1, PERMISSION_ID_2);

  public static final RoleDto ROLE_DTO_1 = RoleDto.builder()
      .id(ROLE_ID_1)
      .name(ROLE_NAME_1)
      .description(ROLE_DESCRIPTION_1)
      .build();

  public static final RoleDto ROLE_DTO_2 = RoleDto.builder()
      .id(ROLE_ID_2)
      .name(ROLE_NAME_2)
      .description(ROLE_DESCRIPTION_2)
      .build();

  public static final List<RoleDto> ROLE_DTOS_LIST = List.of(ROLE_DTO_1, ROLE_DTO_2);

  public static final RoleDetailsDto ROLE_DETAILS = RoleDetailsDto.builder()
      .id(ROLE_ID_1)
      .name(ROLE_NAME_1)
      .description(ROLE_DESCRIPTION_1)
      .permissions(PERMISSION_IDS)
      .createdAt(CREATED_AT)
      .createdBy(USERNAME_1)
      .updatedAt(UPDATED_AT)
      .updatedBy(USERNAME_1)
      .build();

  public static final RemoveRoleRequest REMOVE_ROLE_REQUEST = RemoveRoleRequest.builder()
      .id(ROLE_ID_1)
      .deletedBy(USERNAME_1)
      .build();

  public static final CreateRoleDto CREATE_ROLE_DTO = CreateRoleDto.builder()
      .id(ROLE_ID_1)
      .name(ROLE_NAME_1)
      .description(ROLE_DESCRIPTION_1)
      .permissions(PERMISSION_IDS)
      .build();

  public static final CreateRoleDto CREATE_ROLE_DTO_WITH_TOO_LONG_NAME = CreateRoleDto.builder()
      .id(ROLE_ID_1)
      .name(ROLE_NAME_TOO_LONG)
      .description(ROLE_DESCRIPTION_1)
      .permissions(PERMISSION_IDS)
      .build();

  public static final CreateRoleDto CREATE_ROLE_DTO_WITH_TOO_SHORT_NAME = CreateRoleDto.builder()
      .id(ROLE_ID_1)
      .name(ROLE_NAME_WITH_LESS_THAN_MIN_NUMBER_OF_CHAR)
      .description(ROLE_DESCRIPTION_1)
      .permissions(PERMISSION_IDS)
      .build();

  public static final CreateRoleDto CREATE_ROLE_DTO_WITH_DESCRIPTION_TOO_SHORT =
      CreateRoleDto.builder()
          .id(ROLE_ID_1)
          .name(ROLE_NAME_1)
          .description(ROLE_DESCRIPTION_WITH_LESS_CHARS_THEN_REQUIRED)
          .permissions(PERMISSION_IDS)
          .build();

  public static final CreateRoleDto CREATE_ROLE_DTO_WITH_DESCRIPTION_TOO_LONG =
      CreateRoleDto.builder()
          .id(ROLE_ID_1)
          .name(ROLE_NAME_1)
          .description(ROLE_DESCRIPTION_WITH_MORE_CHARS_THEN_PERMITTED)
          .permissions(PERMISSION_IDS)
          .build();

  public static final CreateRoleRequest CREATE_ROLE_REQUEST = CreateRoleRequest.builder()
      .id(ROLE_ID_1)
      .name(ROLE_NAME_1)
      .description(ROLE_DESCRIPTION_1)
      .permissions(PERMISSION_IDS)
      .createdBy(USERNAME_1)
      .build();
}
