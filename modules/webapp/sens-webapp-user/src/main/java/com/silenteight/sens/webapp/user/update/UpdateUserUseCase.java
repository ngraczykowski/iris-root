package com.silenteight.sens.webapp.user.update;

import lombok.*;
import lombok.Builder.Default;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.audit.trace.AuditTracer;
import com.silenteight.sens.webapp.user.domain.validator.NameLengthValidator;
import com.silenteight.sens.webapp.user.domain.validator.NameLengthValidator.InvalidNameLengthError;
import com.silenteight.sens.webapp.user.domain.validator.RolesValidator;
import com.silenteight.sens.webapp.user.domain.validator.RolesValidator.RolesDontExistError;
import com.silenteight.sens.webapp.user.update.UpdatedUser.UpdatedUserBuilder;
import com.silenteight.sep.base.common.time.DefaultTimeSource;
import com.silenteight.sep.base.common.time.TimeSource;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.vavr.control.Option;

import java.util.Optional;
import java.util.Set;
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

  public void apply(UpdateUserCommand command) {
    log.info(USER_MANAGEMENT, "Updating user. command={}", command);

    auditTracer.save(new UserUpdateRequestedEvent(
        command.getUsername(), UpdateUserCommand.class.getName(), command));

    command.getDisplayName().ifPresent(this::validateDisplayName);
    command.getRoles().ifPresent(this::validateRoles);
    update(command);
  }

  private void validateRoles(Set<String> roles) {
    if (roles.isEmpty())
      return;

    Option<RolesDontExistError> validationError = rolesValidator.validate(roles);
    if (validationError.isDefined())
      throw validationError.get().toException();
  }

  private void validateDisplayName(String displayName) {
    Option<InvalidNameLengthError> validationError =
        displayNameLengthValidator.validate(displayName);
    if (validationError.isDefined())
      throw validationError.get().toException();
  }

  private void update(UpdateUserCommand command) {
    updatedUserRepository.save(command.toUpdatedUser());
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

    UpdatedUser toUpdatedUser() {
      UpdatedUserBuilder result = UpdatedUser
          .builder()
          .username(username)
          .updateDate(timeSource.offsetDateTime());

      if (displayName != null)
        result.displayName(displayName);
      if (roles != null)
        result.roles(roles);

      return result.build();
    }
  }
}
