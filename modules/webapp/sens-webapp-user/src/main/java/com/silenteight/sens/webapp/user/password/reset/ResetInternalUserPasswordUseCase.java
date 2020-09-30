package com.silenteight.sens.webapp.user.password.reset;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.audit.api.trace.AuditTracer;
import com.silenteight.sep.usermanagement.api.ResettableUserCredentials;
import com.silenteight.sep.usermanagement.api.TemporaryPassword;
import com.silenteight.sep.usermanagement.api.TemporaryPasswordGenerator;
import com.silenteight.sep.usermanagement.api.UserCredentialsRepository;

import static com.silenteight.sens.webapp.logging.SensWebappLogMarkers.USER_MANAGEMENT;

@RequiredArgsConstructor
@Slf4j
public class ResetInternalUserPasswordUseCase {

  @NonNull
  private final UserCredentialsRepository credentialsRepository;
  @NonNull
  private final TemporaryPasswordGenerator temporaryPasswordGenerator;
  @NonNull
  private final AuditTracer auditTracer;

  public TemporaryPassword execute(String username) {
    log.info(USER_MANAGEMENT, "Reset password. username={}", username);

    auditTracer.save(new PasswordResetRequestedEvent(username, TemporaryPassword.class.getName()));

    ResettableUserCredentials userCredentials = credentialsRepository
        .findUserCredentials(username)
        .orElseThrow(() -> new UserNotFoundException(username));

    if (userCredentials.ownerIsNotInternal())
      throw new UserIsNotInternalException(username);

    log.info(USER_MANAGEMENT, "Resetting password for user. credentials={}", userCredentials);
    TemporaryPassword temporaryPassword = resetPassword(userCredentials);

    auditTracer.save(new PasswordResetEvent(username, TemporaryPassword.class.getName()));

    return temporaryPassword;
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
