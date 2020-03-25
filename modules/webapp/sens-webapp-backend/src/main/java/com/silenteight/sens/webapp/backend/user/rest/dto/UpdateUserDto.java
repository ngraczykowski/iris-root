package com.silenteight.sens.webapp.backend.user.rest.dto;

import lombok.*;

import com.silenteight.sens.webapp.user.update.UpdateUserUseCase.UpdateUserCommand;

import java.util.Set;
import javax.annotation.Nullable;

@Builder
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UpdateUserDto {

  @Nullable
  private String displayName;

  @Nullable
  private Set<String> roles;

  public UpdateUserCommand toCommand(String username) {
    return UpdateUserCommand
        .builder()
        .username(username)
        .displayName(displayName)
        .roles(roles)
        .build();
  }
}
