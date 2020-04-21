package com.silenteight.sens.webapp.user.update;

import lombok.*;
import lombok.Builder.Default;

import com.silenteight.sens.webapp.audit.api.AuditLog;
import com.silenteight.sens.webapp.common.time.DefaultTimeSource;
import com.silenteight.sens.webapp.common.time.TimeSource;
import com.silenteight.sens.webapp.user.domain.validator.NameLengthValidator;
import com.silenteight.sens.webapp.user.domain.validator.NameLengthValidator.InvalidNameLengthError;
import com.silenteight.sens.webapp.user.domain.validator.RolesValidator;
import com.silenteight.sens.webapp.user.domain.validator.RolesValidator.RolesDontExistError;
import com.silenteight.sens.webapp.user.update.UpdatedUser.UpdatedUserBuilder;

import io.vavr.control.Option;

import java.util.Optional;
import java.util.Set;
import javax.annotation.Nullable;

import static com.silenteight.sens.webapp.audit.api.AuditMarker.USER_MANAGEMENT;
import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
public class UpdateUserUseCase {

  @NonNull
  private final UpdatedUserRepository updatedUserRepository;

  @NonNull
  private final NameLengthValidator displayNameLengthValidator;

  @NonNull
  private final RolesValidator rolesValidator;

  @NonNull
  private final AuditLog auditLog;

  public void apply(UpdateUserCommand command) {
    auditLog.logInfo(USER_MANAGEMENT, "Updating user. command={}", command);

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
    private String displayName;

    @Nullable
    private Set<String> roles;

    @Default
    private TimeSource timeSource = DefaultTimeSource.INSTANCE;

    Optional<String> getDisplayName() {
      return ofNullable(displayName);
    }

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
