package com.silenteight.sens.webapp.user.password;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class ResetInternalUserPasswordUseCase {

  private final UserCredentialsRepository credentialsRepo;
  private final TemporaryPasswordGenerator temporaryPasswordGenerator;

  public TemporaryPassword execute(String username) {
    log.debug("Reset password for user {} use case", username);

    ResettableUserCredentials userCredentials = credentialsRepo
        .findUserCredentials(username)
        .orElseThrow(() -> new UserNotFoundException(username));

    if (userCredentials.ownerIsNotInternal())
      throw new UserIsNotInternalException(username);

    log.debug("Resetting password for user {}", userCredentials);
    return resetPassword(userCredentials);
  }

  private TemporaryPassword resetPassword(ResettableUserCredentials credentials) {
    TemporaryPassword password = temporaryPasswordGenerator.generate();
    credentials.reset(password);

    return password;
  }

  public static class UserNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -6412227439390462594L;

    UserNotFoundException(String username) {
      super("User " + username + " could not be found.");
    }
  }

  public static class UserIsNotInternalException extends RuntimeException {

    private static final long serialVersionUID = -8628386417991264784L;

    UserIsNotInternalException(String username) {
      super("User " + username + " is not an internal user.");
    }
  }
}
