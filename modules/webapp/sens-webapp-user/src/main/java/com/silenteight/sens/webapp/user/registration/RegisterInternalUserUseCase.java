package com.silenteight.sens.webapp.user.registration;

import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import com.silenteight.sens.webapp.user.registration.domain.*;
import com.silenteight.sens.webapp.user.registration.domain.NewUserDetails.Credentials;

import io.vavr.control.Either;

import java.util.Set;
import javax.annotation.Nullable;

import static java.util.Collections.emptySet;

@RequiredArgsConstructor
public class RegisterInternalUserUseCase {

  private final UserRegisteringDomainService userRegisteringService;
  private final RegisteredUserRepository registeredUserRepository;

  public Either<UserRegistrationDomainError, Success> apply(RegisterInternalUserCommand command) {
    NewUserRegistration newUserRegistration = command.toUserRegistration();

    Either<UserRegistrationDomainError, CompletedUserRegistration> registeringResult =
        userRegisteringService.register(newUserRegistration);

    registeringResult.forEach(registeredUserRepository::save);

    return registeringResult.map(completedRegistration -> completedRegistration::getUsername);
  }

  public interface Success {

    String getUsername();
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
          new NewUserDetails(username, displayName, new Credentials(password), roles),
          RegistrationSource.INTERNAL
      );
    }
  }
}
