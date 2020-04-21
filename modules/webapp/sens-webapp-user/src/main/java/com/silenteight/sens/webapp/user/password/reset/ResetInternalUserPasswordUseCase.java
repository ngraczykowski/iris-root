package com.silenteight.sens.webapp.user.password.reset;

import lombok.RequiredArgsConstructor;

import com.silenteight.sens.webapp.audit.api.AuditLog;

import static com.silenteight.sens.webapp.audit.api.AuditMarker.USER_MANAGEMENT;


@RequiredArgsConstructor
public class ResetInternalUserPasswordUseCase {

  private final UserCredentialsRepository credentialsRepository;
  private final TemporaryPasswordGenerator temporaryPasswordGenerator;
  private final AuditLog auditLog;

  public TemporaryPassword execute(String username) {
    auditLog.logInfo(USER_MANAGEMENT, "Reset password. username={}", username);

    ResettableUserCredentials userCredentials = credentialsRepository
        .findUserCredentials(username)
        .orElseThrow(() -> new UserNotFoundException(username));

    if (userCredentials.ownerIsNotInternal())
      throw new UserIsNotInternalException(username);

    auditLog.logInfo(
        USER_MANAGEMENT, "Resetting password for user. credentials={}", userCredentials);
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
