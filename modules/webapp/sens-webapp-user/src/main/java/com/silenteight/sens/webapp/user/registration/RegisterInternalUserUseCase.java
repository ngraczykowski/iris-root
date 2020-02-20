package com.silenteight.sens.webapp.user.registration;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.user.registration.domain.NewUserDetails;
import com.silenteight.sens.webapp.user.registration.domain.NewUserDetails.Credentials;
import com.silenteight.sens.webapp.user.registration.domain.NewUserRegistration;
import com.silenteight.sens.webapp.user.registration.domain.UserRegisteringDomainService;
import com.silenteight.sens.webapp.user.registration.domain.UserRegistrationDomainError;

import io.vavr.control.Either;

import java.util.Set;
import javax.annotation.Nullable;

import static com.silenteight.sens.webapp.user.registration.domain.RegistrationSource.INTERNAL;
import static java.util.Collections.emptySet;
import static java.util.Optional.of;

@Slf4j
public class RegisterInternalUserUseCase extends BaseRegisterUserUseCase {

  public RegisterInternalUserUseCase(
      UserRegisteringDomainService userRegisteringDomainService,
      RegisteredUserRepository registeredUserRepository) {

    super(userRegisteringDomainService, registeredUserRepository);
  }

  public Either<UserRegistrationDomainError, Success> apply(RegisterInternalUserCommand command) {
    log.debug("Registering internal user. command={}", command);

    return register(command.toUserRegistration());
  }

  @Value
  @Builder
  public static class RegisterInternalUserCommand {

    @NonNull
    private final String username;
    @NonNull
    private final String password;
    @Nullable
    private final String displayName;
    @NonNull
    @Builder.Default
    private final Set<String> roles = emptySet();

    NewUserRegistration toUserRegistration() {
      return new NewUserRegistration(
          new NewUserDetails(username, displayName, of(new Credentials(password)), roles),
          INTERNAL);
    }
  }
}
