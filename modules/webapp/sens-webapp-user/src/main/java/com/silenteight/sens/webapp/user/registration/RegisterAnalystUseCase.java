package com.silenteight.sens.webapp.user.registration;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import com.silenteight.sens.webapp.user.registration.domain.NewUserDetails;
import com.silenteight.sens.webapp.user.registration.domain.NewUserRegistration;
import com.silenteight.sens.webapp.user.registration.domain.UserRegisteringDomainService;
import com.silenteight.sens.webapp.user.registration.domain.UserRegistrationDomainError;

import io.vavr.control.Either;

import javax.annotation.Nullable;

import static com.silenteight.sens.webapp.user.domain.UserRole.ANALYST;
import static com.silenteight.sens.webapp.user.registration.domain.RegistrationSource.GNS;
import static java.util.Collections.singleton;
import static java.util.Optional.empty;

public class RegisterAnalystUseCase extends BaseRegisterUserUseCase {

  public RegisterAnalystUseCase(
      UserRegisteringDomainService userRegisteringDomainService,
      RegisteredUserRepository registeredUserRepository) {

    super(userRegisteringDomainService, registeredUserRepository);
  }

  public Either<UserRegistrationDomainError, Success> apply(RegisterAnalystUserCommand command) {
    return register(command.toUserRegistration());
  }

  @Value
  @Builder
  public static class RegisterAnalystUserCommand {

    @NonNull
    private final String username;
    @Nullable
    private final String displayName;

    NewUserRegistration toUserRegistration() {
      return new NewUserRegistration(
          new NewUserDetails(username, displayName, empty(), singleton(ANALYST)),
          GNS);
    }
  }
}
