package com.silenteight.sep.usermanagement.keycloak.query;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.base.common.time.TimeConverter;
import com.silenteight.sep.usermanagement.api.UserQuery;
import com.silenteight.sep.usermanagement.api.dto.UserDto;
import com.silenteight.sep.usermanagement.keycloak.query.lastlogintime.LastLoginTimeProvider;
import com.silenteight.sep.usermanagement.keycloak.query.role.RolesProvider;

import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static com.silenteight.sep.usermanagement.api.origin.SensOrigin.SENS_ORIGIN;
import static com.silenteight.sep.usermanagement.keycloak.KeycloakUserAttributeNames.LOCKED_AT;
import static com.silenteight.sep.usermanagement.keycloak.KeycloakUserAttributeNames.USER_ORIGIN;
import static java.lang.Integer.MAX_VALUE;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toUnmodifiableList;

@Slf4j
@RequiredArgsConstructor
public class KeycloakUserQuery implements UserQuery {

  private final UsersResource usersResource;
  private final LastLoginTimeProvider lastLoginTimeProvider;
  private final RolesProvider userRolesProvider;
  private final TimeConverter timeConverter;

  @Override
  public List<UserDto> listAll() {
    log.info("Listing all users");
    return usersResource
        .list(0, MAX_VALUE)
        .stream()
        .map(this::mapToDto)
        .collect(toUnmodifiableList());
  }

  @Override
  public Optional<UserDto> find(String username) {
    return usersResource
        .search(username)
        .stream()
        .map(this::mapToDto)
        .findFirst();
  }

  UserDto mapToDto(UserRepresentation userRepresentation) {
    UserDto userDto = new UserDto();

    userDto.setCreatedAt(
        timeConverter.toOffsetFromMilli(userRepresentation.getCreatedTimestamp()));
    userDto.setDisplayName(userRepresentation.getFirstName());
    userDto.setUserName(userRepresentation.getUsername());
    userDto.setLockedAt(getDeletedAt(userRepresentation));

    String userId = userRepresentation.getId();

    lastLoginTimeProvider
        .getForUserId(userId)
        .ifPresent(userDto::setLastLoginAt);

    userDto.setRoles(userRolesProvider.getForUserId(userId));
    userDto.setOrigin(getOrigin(userRepresentation));

    return userDto;
  }

  private static String getOrigin(UserRepresentation userRepresentation) {
    return getAttribute(userRepresentation, USER_ORIGIN)
        .orElse(SENS_ORIGIN);
  }

  private static OffsetDateTime getDeletedAt(UserRepresentation userRepresentation) {
    return getAttribute(userRepresentation, LOCKED_AT)
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
}
