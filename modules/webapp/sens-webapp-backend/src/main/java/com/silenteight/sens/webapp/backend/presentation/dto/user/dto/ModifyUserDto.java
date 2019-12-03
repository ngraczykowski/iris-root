package com.silenteight.sens.webapp.backend.presentation.dto.user.dto;

import lombok.*;

import com.silenteight.sens.webapp.kernel.security.authority.Role;
import com.silenteight.sens.webapp.users.user.dto.UpdateUserRequest;

import java.util.List;
import javax.annotation.Nullable;

@Builder
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ModifyUserDto {

  @Nullable
  private String password;

  @Nullable
  private String displayName;

  @Nullable
  private Boolean superUser;

  @Nullable
  private Boolean active;

  @Nullable
  private List<Role> roles;

  public UpdateUserRequest getDomainRequest(long userId) {
    return UpdateUserRequest
        .builder()
        .userId(userId)
        .displayName(getDisplayName())
        .password(getPassword())
        .superUser(getSuperUser())
        .active(getActive())
        .roles(getRoles())
        .build();
  }
}
