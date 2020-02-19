package com.silenteight.sens.webapp.user.update;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.util.Optional;
import javax.annotation.Nullable;

import static java.util.Optional.ofNullable;

@Data
@Builder
public class UpdatedUser {

  @NonNull
  private final String username;
  @Nullable
  private final String displayName;

  public String getUsername() {
    return username;
  }

  public Optional<String> getDisplayName() {
    return ofNullable(displayName);
  }
}
