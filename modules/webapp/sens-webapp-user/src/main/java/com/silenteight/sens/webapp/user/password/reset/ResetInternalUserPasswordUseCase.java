package com.silenteight.sens.webapp.user.password.reset;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.silenteight.sens.webapp.logging.SensWebappLogMarkers.USER_MANAGEMENT;

@RequiredArgsConstructor
@Slf4j
public class ResetInternalUserPasswordUseCase {

  private final UserCredentialsRepository credentialsRepository;
  private final TemporaryPasswordGenerator temporaryPasswordGenerator;

  public TemporaryPassword execute(String username) {
    log.info(USER_MANAGEMENT, "Reset password. username={}", username);

    ResettableUserCredentials userCredentials = credentialsRepository
        .findUserCredentials(username)
        .orElseThrow(() -> new UserNotFoundException(username));

    if (userCredentials.ownerIsNotInternal())
      throw new UserIsNotInternalException(username);

    log.info(USER_MANAGEMENT, "Resetting password for user. credentials={}", userCredentials);
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
