package com.silenteight.sens.webapp.backend.users.rest.dto;

import lombok.*;

import java.util.Set;
import javax.annotation.Nullable;

@Builder
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ModifyUserDto {

  @Nullable
  @ToString.Exclude
  private String password;

  @Nullable
  private String displayName;

  @Nullable
  private Boolean active;

  @Nullable
  private Set<String> roles;
}
