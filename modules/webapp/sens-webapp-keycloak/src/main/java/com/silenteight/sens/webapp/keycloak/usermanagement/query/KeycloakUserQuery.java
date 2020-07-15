package com.silenteight.sens.webapp.keycloak.usermanagement.query;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.keycloak.usermanagement.query.lastlogintime.LastLoginTimeProvider;
import com.silenteight.sens.webapp.keycloak.usermanagement.query.role.RolesProvider;
import com.silenteight.sens.webapp.user.UserListQuery;
import com.silenteight.sens.webapp.user.UserQuery;
import com.silenteight.sens.webapp.user.dto.UserDto;
import com.silenteight.sep.base.common.time.TimeConverter;

import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static com.silenteight.sens.webapp.keycloak.usermanagement.KeycloakUserAttributeNames.DELETED_AT;
import static com.silenteight.sens.webapp.keycloak.usermanagement.KeycloakUserAttributeNames.USER_ORIGIN;
import static com.silenteight.sens.webapp.logging.SensWebappLogMarkers.USER_MANAGEMENT;
import static com.silenteight.sens.webapp.user.domain.SensOrigin.SENS_ORIGIN;
import static java.lang.Integer.MAX_VALUE;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toUnmodifiableList;
import static org.apache.commons.lang3.BooleanUtils.isTrue;

@Slf4j
@RequiredArgsConstructor
public class KeycloakUserQuery implements UserQuery, UserListQuery {

  private final UsersResource usersResource;
  //private final LastLoginTimeProvider lastLoginTimeProvider;
  private final RolesProvider userRolesProvider;
  private final TimeConverter timeConverter;

  @Override
  public Page<UserDto> listEnabled(Pageable pageable) {
    Collection<UserRepresentation> enabledUsers = fetchEnabledUsers();

    List<UserDto> usersPage = enabledUsers
        .stream()
        .skip(pageable.getOffset())
        .limit(pageable.getPageSize())
        .map(this::mapToDto)
        .collect(toList());

    return new PageImpl<>(usersPage, pageable, enabledUsers.size());
  }

  @Override
  public Optional<UserDto> find(String username) {
    return usersResource
        .search(username)
        .stream()
        .map(this::mapToDto)
        .findFirst();
  }

  private Collection<UserRepresentation> fetchEnabledUsers() {
    return usersResource
        .list(0, MAX_VALUE)
        .stream()
        .filter(user -> isTrue(user.isEnabled()))
        .collect(toUnmodifiableList());
  }

  UserDto mapToDto(UserRepresentation userRepresentation) {
    UserDto userDto = new UserDto();

    userDto.setCreatedAt(
        timeConverter.toOffsetFromMilli(userRepresentation.getCreatedTimestamp()));
    userDto.setDisplayName(userRepresentation.getFirstName());
    userDto.setUserName(userRepresentation.getUsername());
    userDto.setDeletedAt(getDeletedAt(userRepresentation));

    String userId = userRepresentation.getId();

/*    lastLoginTimeProvider
        .getForUserId(userId)
        .ifPresent(userDto::setLastLoginAt);*/

    userDto.setRoles(userRolesProvider.getForUserId(userId));
    userDto.setOrigin(getOrigin(userRepresentation));

    return userDto;
  }

  private static String getOrigin(UserRepresentation userRepresentation) {
    return getAttribute(userRepresentation, USER_ORIGIN)
        .orElse(SENS_ORIGIN);
  }

  private static OffsetDateTime getDeletedAt(UserRepresentation userRepresentation) {
    return getAttribute(userRepresentation, DELETED_AT)
        .map(OffsetDateTime::parse)
        .orElse(null);
  }

  private static Optional<String> getAttribute(
      UserRepresentation userRepresentation, String attributeName) {

    return ofNullable(userRepresentation.getAttributes())
        .map(attributes -> attributes.getOrDefault(attributeName, emptyList()))
        .filter(attribute -> !attribute.isEmpty())
        .map(attribute -> attribute.get(0));
  }

  @Override
  public Collection<UserDto> listEnabled() {
    return fetchEnabledUsers()
        .stream()
        .map(this::mapToDto)
        .collect(toUnmodifiableList());
  }

  @Override
  public Collection<UserDto> listAll() {
    log.info(USER_MANAGEMENT, "Listing all users");
    return usersResource
        .list(0, MAX_VALUE)
        .stream()
        .map(this::mapToDto)
        .collect(toUnmodifiableList());
  }
}
