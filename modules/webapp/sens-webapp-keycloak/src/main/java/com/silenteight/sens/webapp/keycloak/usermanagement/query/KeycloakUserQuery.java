package com.silenteight.sens.webapp.keycloak.usermanagement.query;

import lombok.RequiredArgsConstructor;

import com.silenteight.sens.webapp.common.time.TimeConverter;
import com.silenteight.sens.webapp.keycloak.usermanagement.query.lastlogintime.LastLoginTimeProvider;
import com.silenteight.sens.webapp.keycloak.usermanagement.query.role.RolesProvider;
import com.silenteight.sens.webapp.user.UserListQuery;
import com.silenteight.sens.webapp.user.UserQuery;
import com.silenteight.sens.webapp.user.dto.UserDto;

import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;

import static com.silenteight.sens.webapp.user.domain.UserOrigin.SENS;
import static java.lang.Integer.MAX_VALUE;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toUnmodifiableList;

@RequiredArgsConstructor
public class KeycloakUserQuery implements UserQuery, UserListQuery {

  private final UsersResource usersResource;
  private final LastLoginTimeProvider lastLoginTimeProvider;
  private final RolesProvider userRolesProvider;
  private final TimeConverter timeConverter;

  @Override
  public Page<UserDto> list(Pageable pageable) {
    List<UserRepresentation> users = getUsers(pageable);

    List<UserDto> usersPage = users.stream()
        .map(this::mapToDto)
        .collect(toList());

    return new PageImpl<>(usersPage, pageable, usersResource.count());
  }

  private List<UserRepresentation> getUsers(Pageable pageable) {
    int pageSize = pageable.getPageSize();
    long offset = pageable.getOffset();

    return usersResource.list((int) offset, pageSize);
  }

  UserDto mapToDto(UserRepresentation userRepresentation) {
    UserDto userDto = new UserDto();

    userDto.setCreatedAt(
        timeConverter.toOffsetFromMilli(userRepresentation.getCreatedTimestamp()));
    userDto.setDisplayName(userRepresentation.getFirstName());
    userDto.setUserName(userRepresentation.getUsername());

    String userId = userRepresentation.getId();

    lastLoginTimeProvider
        .getForUserId(userId)
        .ifPresent(userDto::setLastLoginAt);

    userDto.setRoles(userRolesProvider.getForUserId(userId));
    // WA-344(mmastylo) read origin from UserRepresentation
    userDto.setOrigin(SENS);

    return userDto;
  }

  @Override
  public Collection<UserDto> list() {
    return usersResource
        .list(0, MAX_VALUE)
        .stream()
        .map(this::mapToDto)
        .collect(toUnmodifiableList());
  }
}
