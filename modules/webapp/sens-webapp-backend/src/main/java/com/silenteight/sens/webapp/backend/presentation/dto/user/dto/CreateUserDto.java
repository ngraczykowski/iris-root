package com.silenteight.sens.webapp.backend.presentation.dto.user.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import com.silenteight.sens.webapp.kernel.security.authority.Role;
import com.silenteight.sens.webapp.user.dto.CreateUserRequest;

import java.util.List;

@Builder
@Data
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
