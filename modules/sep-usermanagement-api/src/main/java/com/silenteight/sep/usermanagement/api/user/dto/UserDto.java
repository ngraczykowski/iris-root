package com.silenteight.sep.usermanagement.api.user.dto;

import lombok.*;

import com.silenteight.sep.usermanagement.api.origin.UserOrigin;
import com.silenteight.sep.usermanagement.api.role.UserRoles;

import java.time.OffsetDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

  private String userName;
  private String displayName;
  @NonNull
  private UserRoles roles;
  private OffsetDateTime lastLoginAt;
  private OffsetDateTime createdAt;
  private OffsetDateTime lockedAt;

  @NonNull
  private String origin;

  public boolean hasOnlyRole(String scope, String role) {
    return getRoles().getRoles(scope).size() == 1
        && hasRole(scope, role);
  }

  public boolean hasRole(String scope, String role) {
    return getRoles().getRoles(scope).contains(role);
  }

  public boolean hasOrigin(UserOrigin origin) {
    return this.origin.equals(origin.toString());
  }
}
