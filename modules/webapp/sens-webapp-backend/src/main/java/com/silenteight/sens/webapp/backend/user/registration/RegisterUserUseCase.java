package com.silenteight.sens.webapp.backend.user.registration;

import lombok.*;

import com.silenteight.sens.webapp.backend.user.registration.domain.CompletedUserRegistration;
import com.silenteight.sens.webapp.backend.user.registration.domain.CompletedUserRegistration.NewUserCredentials;
import com.silenteight.sens.webapp.backend.user.registration.domain.CompletedUserRegistration.NewUserDetails;
import com.silenteight.sens.webapp.backend.user.registration.domain.NewUserRegistration;
import com.silenteight.sens.webapp.backend.user.registration.domain.UserRegisteringDomainService;

import io.vavr.control.Either;

import java.util.Set;
import javax.annotation.Nullable;

import static java.util.Collections.emptySet;

@RequiredArgsConstructor
public class RegisterUserUseCase {

  private final UserRegisteringDomainService userRegisteringService;
  private final RegisteredUserRepository registeredUserRepository;

  public Either<UserRegistrationDomainError, Success> apply(RegisterUserCommand command) {
    NewUserRegistration newUserRegistration = command.toUserRegistration();

    Either<UserRegistrationDomainError, CompletedUserRegistration> registeringResult =
        userRegisteringService.register(newUserRegistration);

    registeringResult.forEach(registeredUserRepository::save);

    return registeringResult.map(Success::new);
  }

  @Data
  @RequiredArgsConstructor(access = AccessLevel.NONE)
  public static class Success {

    private final String username;

    Success(CompletedUserRegistration completedUserRegistration) {
      username = completedUserRegistration.getUsername();
    }
  }

  @Value
  @Builder
  public static class RegisterUserCommand {

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
          username,
          new NewUserDetails(displayName, roles),
          new NewUserCredentials(password));
    }
  }
}
