package com.silenteight.sens.webapp.keycloak.usermanagement.query;

import lombok.RequiredArgsConstructor;

import com.silenteight.sens.webapp.common.time.TimeConverter;
import com.silenteight.sens.webapp.keycloak.usermanagement.query.lastlogintime.LastLoginTimeProvider;
import com.silenteight.sens.webapp.user.UserQuery;
import com.silenteight.sens.webapp.user.dto.UserDto;

import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class KeycloakUserQuery implements UserQuery {

  private final UsersResource usersResource;
  private final LastLoginTimeProvider lastLoginTimeProvider;
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
        timeConverter.toOffsetFromSeconds(userRepresentation.getCreatedTimestamp()));
    userDto.setDisplayName(userRepresentation.getFirstName());
    userDto.setUserName(userRepresentation.getUsername());
    userDto.setRoles(userRepresentation.getRealmRoles());

    lastLoginTimeProvider
        .getForUserId(userRepresentation.getId())
        .ifPresent(userDto::setLastLoginAt);

    return userDto;
  }
}
