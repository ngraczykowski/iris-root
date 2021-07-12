package com.silenteight.sens.webapp.user.update;

import lombok.*;
import lombok.Builder.Default;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.audit.api.trace.AuditTracer;
import com.silenteight.sens.webapp.user.domain.validator.NameLengthValidator;
import com.silenteight.sens.webapp.user.domain.validator.NameLengthValidator.InvalidNameLengthError;
import com.silenteight.sens.webapp.user.roles.ScopeUserRoles;
import com.silenteight.sens.webapp.user.roles.UserRolesRetriever;
import com.silenteight.sep.base.common.time.DefaultTimeSource;
import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.sep.usermanagement.api.RolesValidator;
import com.silenteight.sep.usermanagement.api.RolesValidator.RolesDontExistError;
import com.silenteight.sep.usermanagement.api.UpdatedUser;
import com.silenteight.sep.usermanagement.api.UpdatedUser.UpdatedUserBuilder;
import com.silenteight.sep.usermanagement.api.UpdatedUserRepository;
import com.silenteight.sep.usermanagement.api.UserRoles;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.vavr.control.Option;

import java.util.*;
import javax.annotation.Nullable;

import static com.silenteight.sens.webapp.logging.SensWebappLogMarkers.USER_MANAGEMENT;
import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
@Slf4j
public class UpdateUserUseCase {

  @NonNull
  private final UpdatedUserRepository updatedUserRepository;
  @NonNull
  private final NameLengthValidator displayNameLengthValidator;
  @NonNull
  private final RolesValidator rolesValidator;
  @NonNull
  private final AuditTracer auditTracer;
  @NonNull
  private final UserRolesRetriever userRolesRetriever;
  @NonNull
  private final String rolesScope;
  @NonNull
  private final String countryGroupsScope;

  public void apply(UpdateUserCommand command) {
    log.info(USER_MANAGEMENT, "Updating user. command={}", command);

    auditTracer.save(new UserUpdateRequestedEvent(
        command.getUsername(), UpdateUserCommand.class.getName(), command));

    command.getDisplayName().ifPresent(this::validateDisplayName);
    command.getRoles().ifPresent(roles -> this.validateRoles(rolesScope, roles));
    update(command);
  }

  private void validateRoles(String roleScope, Set<String> roles) {
    if (roles.isEmpty())
      return;

    Optional<RolesDontExistError> validationError = rolesValidator.validate(roleScope, roles);
    if (validationError.isPresent())
      throw validationError.get().toException();
  }

  private void validateDisplayName(String displayName) {
    Option<InvalidNameLengthError> validationError =
        displayNameLengthValidator.validate(displayName);

    if (validationError.isDefined())
      throw validationError.get().toException();
  }

  private void update(UpdateUserCommand command) {
    Set<String> currentRoles = userRolesRetriever
        .rolesOf(command.getUsername())
        .getRoles(rolesScope);
    UpdatedUser updatedUser = command.toUpdatedUser(rolesScope, countryGroupsScope);
    updatedUserRepository.save(updatedUser);
    auditTracer.save(
        new UserUpdatedEvent(
            updatedUser.getUsername(),
            UpdatedUser.class.getName(),
            new UpdatedUserDetails(updatedUser, command.getRoles().orElse(null), currentRoles)));
  }

  @Data
  @Builder
  @EqualsAndHashCode(doNotUseGetters = true)
  public static class UpdateUserCommand {

    @NonNull
    private String username;

    @Nullable
    @JsonProperty
    private String displayName;

    @Nullable
    @JsonProperty
    private Set<String> roles;

    @Nullable
    @JsonProperty
    private Set<String> countryGroups;

    @Nullable
    @JsonProperty
    private Boolean locked;

    @Default
    @JsonIgnore
    private TimeSource timeSource = DefaultTimeSource.INSTANCE;

    @JsonIgnore
    Optional<String> getDisplayName() {
      return ofNullable(displayName);
    }

    @JsonIgnore
    Optional<Set<String>> getRoles() {
      return ofNullable(roles);
    }

    @JsonIgnore
    Optional<Set<String>> getCountryGroups() {
      return ofNullable(countryGroups);
    }

    UpdatedUser toUpdatedUser(String rolesClientId, String countryGroupsClientId) {
      UpdatedUserBuilder result = UpdatedUser.builder()
          .username(username)
          .updateDate(timeSource.offsetDateTime());

      if (displayName != null)
        result.displayName(displayName);
      if (locked != null)
        result.locked(locked);

      result.roles(scopeRoles(rolesClientId, countryGroupsClientId));

      return result.build();
    }

    private UserRoles scopeRoles(
        String rolesClientId, String countryGroupsClientId) {

      Map<String, List<String>> scopeRoles = new HashMap<>();
      if (roles != null)
        scopeRoles.put(rolesClientId, new ArrayList<>(roles));
      if (countryGroups != null)
        scopeRoles.put(countryGroupsClientId, new ArrayList<>(countryGroups));

      return new ScopeUserRoles(scopeRoles);
    }
  }
}
