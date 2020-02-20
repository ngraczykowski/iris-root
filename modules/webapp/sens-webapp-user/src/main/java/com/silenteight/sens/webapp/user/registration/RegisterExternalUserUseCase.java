package com.silenteight.sens.webapp.user.registration;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.user.registration.domain.*;

import io.vavr.control.Either;

import java.util.Set;
import javax.annotation.Nullable;

import static java.util.Collections.emptySet;
import static java.util.Optional.empty;

@Slf4j
public class RegisterExternalUserUseCase extends BaseRegisterUserUseCase {

  public RegisterExternalUserUseCase(
      UserRegisteringDomainService userRegisteringDomainService,
      RegisteredUserRepository registeredUserRepository) {

    super(userRegisteringDomainService, registeredUserRepository);
  }

  public Either<UserRegistrationDomainError, Success> apply(RegisterExternalUserCommand command) {
    log.debug("Registering external user. command={}", command);

    return register(command.toUserRegistration());
  }

  @Value
  @Builder
  public static class RegisterExternalUserCommand {

    @NonNull
    private final String username;
    @Nullable
    private final String displayName;
    @NonNull
    @Builder.Default
    private final Set<String> roles = emptySet();
    @NonNull
    private final RegistrationSource source;

    NewUserRegistration toUserRegistration() {
      return new NewUserRegistration(
          new NewUserDetails(username, displayName, empty(), roles),
          source);
    }
  }
}
