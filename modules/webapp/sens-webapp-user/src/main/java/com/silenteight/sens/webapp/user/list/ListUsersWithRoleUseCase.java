package com.silenteight.sens.webapp.user.list;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


import com.silenteight.sep.usermanagement.api.role.UserRoles;
import com.silenteight.sep.usermanagement.api.user.UserQuery;
import com.silenteight.sep.usermanagement.api.user.dto.UserDto;

import java.util.List;

import static com.silenteight.sens.webapp.logging.SensWebappLogMarkers.USER_MANAGEMENT;
import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
public class ListUsersWithRoleUseCase {

  @NonNull
  private final UserQuery userQuery;
  @NonNull
  private final String countryGroupsScope;

  public List<UserListDto> apply(String roleName) {
    log.info(USER_MANAGEMENT, "Listing users with roleName={}", roleName);

    return userQuery
        .listAll(roleName, countryGroupsScope)
        .stream()
        .map(this::toListDto)
        .collect(toList());
  }

  private UserListDto toListDto(UserDto user) {
    return UserListDto.builder()
        .userName(user.getUserName())
        .displayName(user.getDisplayName())
        .origin(user.getOrigin())
        .countryGroups(scopeRoles(countryGroupsScope, user.getRoles()))
        .lastLoginAt(user.getLastLoginAt())
        .createdAt(user.getCreatedAt())
        .lockedAt(user.getLockedAt())
        .build();
  }

  private static List<String> scopeRoles(String scope, UserRoles roles) {
    return roles.getSortedRoles(scope);
  }
}
