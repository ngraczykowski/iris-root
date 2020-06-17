package com.silenteight.sens.webapp.user.registration.domain;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import com.silenteight.sens.webapp.user.registration.domain.NewUserDetails.Credentials;

import java.time.OffsetDateTime;
import java.util.Set;

import static com.silenteight.sens.webapp.audit.trace.AuditEventUtils.OBFUSCATED_STRING;

@Value
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class CompletedUserRegistration {

  @NonNull
  NewUserDetails userDetails;
  @NonNull
  String origin;
  @NonNull
  OffsetDateTime registrationDate;

  public Set<String> getRoles() {
    return userDetails.getRoles();
  }

  public String getDisplayName() {
    return userDetails.getDisplayName();
  }

  public String getUsername() {
    return userDetails.getUsername();
  }

  public Credentials getCredentials() {
    return userDetails.getCredentials();
  }

  public CompletedUserRegistration toCompletedUserRegistrationEvent() {
    NewUserDetails userDetailsForEvent = new NewUserDetails(
        this.userDetails.getUsername(),
        this.userDetails.getDisplayName(),
        new NewUserDetails.Credentials(OBFUSCATED_STRING),
        this.userDetails.getRoles());
    return new CompletedUserRegistration(userDetailsForEvent, this.origin, this.registrationDate);
  }
}
