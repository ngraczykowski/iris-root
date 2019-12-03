package com.silenteight.sens.webapp.backend.presentation.dto.user.dto;

import lombok.*;

import com.silenteight.sens.webapp.kernel.security.authority.Role;
import com.silenteight.sens.webapp.users.user.dto.CreateUserRequest;

import java.util.List;

@Builder
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateUserDto {

  @NonNull
  private String userName;

  private String password;

  private String displayName;

  private boolean superUser;

  private List<Role> roles;

  public CreateUserRequest getDomainRequest() {
    return CreateUserRequest
        .builder()
        .name(getUserName())
        .password(getPassword())
        .displayName(getDisplayName())
        .superUser(isSuperUser())
        .roles(getRoles())
        .build();
  }
}
