package com.silenteight.sens.webapp.user.password.reset;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.audit.api.trace.AuditTracer;
import com.silenteight.sep.usermanagement.api.credentials.TemporaryPasswordGenerator;
import com.silenteight.sep.usermanagement.api.credentials.UserCredentialsQuery;
import com.silenteight.sep.usermanagement.api.credentials.UserCredentialsResetter;
import com.silenteight.sep.usermanagement.api.credentials.dto.TemporaryPassword;

import static com.silenteight.sens.webapp.logging.SensWebappLogMarkers.USER_MANAGEMENT;

@RequiredArgsConstructor
@Slf4j
public class ResetInternalUserPasswordUseCase {

  @NonNull
  private final UserCredentialsQuery userCredentialsQuery;
  @NonNull
  private final TemporaryPasswordGenerator temporaryPasswordGenerator;
  @NonNull
  private final AuditTracer auditTracer;

  public TemporaryPassword execute(String username) {
    log.info(USER_MANAGEMENT, "Reset password. username={}", username);

    auditTracer.save(new PasswordResetRequestedEvent(username, TemporaryPassword.class.getName()));

    UserCredentialsResetter userCredentials = userCredentialsQuery
        .findByUsername(username)
        .orElseThrow(() -> new UserNotFoundException(username));

    if (userCredentials.ownerIsNotInternal())
      throw new UserIsNotInternalException(username);

    log.info(USER_MANAGEMENT, "Resetting password for user. credentials={}", userCredentials);
    TemporaryPassword temporaryPassword = resetPassword(userCredentials);

    auditTracer.save(new PasswordResetEvent(username, TemporaryPassword.class.getName()));

    return temporaryPassword;
  }

  private TemporaryPassword resetPassword(UserCredentialsResetter credentials) {
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
