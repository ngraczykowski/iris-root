package com.silenteight.sens.webapp.user.dto;

import lombok.*;
import lombok.Builder.Default;

import com.silenteight.sens.webapp.user.domain.UserOrigin;

import java.time.OffsetDateTime;
import java.util.List;

import static com.silenteight.sens.webapp.user.domain.UserOrigin.SENS;
import static java.util.Collections.emptyList;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

  private String userName;
  private String displayName;
  @NonNull
  @Default
  private List<String> roles = emptyList();
  private OffsetDateTime lastLoginAt;
  private OffsetDateTime createdAt;
  private boolean isActive;
  @NonNull
  private UserOrigin origin;

  public boolean hasRole(String role) {
    return roles.contains(role);
  }

  public boolean hasOnlyRole(String role) {
    return roles.size() == 1 && hasRole(role);
  }

  public boolean isExternalUser() {
    return origin != SENS;
  }
}
