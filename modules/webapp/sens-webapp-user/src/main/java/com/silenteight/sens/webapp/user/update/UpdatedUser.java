package com.silenteight.sens.webapp.user.update;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.util.Optional;
import java.util.Set;
import javax.annotation.Nullable;

import static java.util.Optional.ofNullable;

@Data
@Builder
public class UpdatedUser {

  @NonNull
  private final String username;
  @Nullable
  private final String displayName;
  @Nullable
  private final Set<String> roles;

  public String getUsername() {
    return username;
  }

  public Optional<String> getDisplayName() {
    return ofNullable(displayName);
  }

  public Optional<Set<String>> getRoles() {
    return ofNullable(roles);
  }
}
