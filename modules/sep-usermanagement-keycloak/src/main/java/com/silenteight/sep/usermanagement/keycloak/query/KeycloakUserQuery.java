package com.silenteight.sep.usermanagement.keycloak.query;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.base.common.time.TimeConverter;
import com.silenteight.sep.usermanagement.api.user.UserQuery;
import com.silenteight.sep.usermanagement.api.user.dto.UserDto;
import com.silenteight.sep.usermanagement.keycloak.query.client.ClientQuery;
import com.silenteight.sep.usermanagement.keycloak.query.lastlogintime.LastLoginTimeProvider;
import com.silenteight.sep.usermanagement.keycloak.query.role.RolesProvider;

import org.keycloak.admin.client.resource.ClientsResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.silenteight.sep.usermanagement.api.origin.SensOrigin.SENS_ORIGIN;
import static com.silenteight.sep.usermanagement.keycloak.KeycloakUserAttributeNames.LOCKED_AT;
import static com.silenteight.sep.usermanagement.keycloak.KeycloakUserAttributeNames.USER_ORIGIN;
import static java.lang.Integer.MAX_VALUE;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.Set.of;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toUnmodifiableList;

@Slf4j
@RequiredArgsConstructor
public class KeycloakUserQuery implements UserQuery {

  @NonNull
  private final UsersResource usersResource;
  @NonNull
  private final LastLoginTimeProvider lastLoginTimeProvider;
  @NonNull
  private final RolesProvider userRolesProvider;
  @NonNull
  private final TimeConverter timeConverter;
  @NonNull
  private final ClientsResource clientsResource;
  @NonNull
  private final ClientQuery clientQuery;
  @NonNull
  private final UsersListFilter usersListFilter;

  @Override
  public List<UserDto> listAll(Set<String> roleScopes) {
    log.info("Listing all users");
    return usersResource
        .list(0, MAX_VALUE)
        .stream()
        .filter(usersListFilter::isVisible)
        .map(userRepresentation -> mapToDto(userRepresentation, roleScopes))
        .collect(toUnmodifiableList());
  }

  @Override
  public List<UserDto> listAll(String roleName, String roleScope) {
    log.info("Listing all users with role name={}", roleName);
    return clientsResource
        .get(clientQuery.getByClientId(roleScope).getId())
        .roles()
        .get(roleName)
        .getRoleUserMembers()
        .stream()
        .filter(usersListFilter::isVisible)
        .map(userRepresentation -> mapToDto(userRepresentation, of(roleScope)))
        .collect(toList());
  }

  @Override
  public Optional<UserDto> find(String username, Set<String> roleScopes) {
    return usersResource
        .search(username)
        .stream()
        .map(userRepresentation -> mapToDto(userRepresentation, roleScopes))
        .findFirst();
  }

  UserDto mapToDto(UserRepresentation userRepresentation, Set<String> roleClientIds) {
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

    userDto.setRoles(userRolesProvider.getForUserId(userId, roleClientIds));
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
