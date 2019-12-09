package com.silenteight.sens.webapp.users.user.dto;

import lombok.*;

import com.silenteight.sens.webapp.kernel.security.authority.Role;

import java.util.List;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class CreateUserRequest {

  @NonNull
  private String name;
  private String password;
  private String displayName;
  private boolean superUser;
  private List<Role> roles;
}
