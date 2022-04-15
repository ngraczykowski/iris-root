package com.silenteight.sens.webapp.backend.user.rest.dto;

import lombok.*;

import com.silenteight.sens.webapp.user.update.UpdateUserUseCase.UpdateUserRequest;

import java.util.Set;
import javax.annotation.Nullable;

import static java.util.Collections.emptySet;

@Builder
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UpdateUserDto {

  @Nullable
  private String displayName;

  @Nullable
  private Set<String> roles;

  @Nullable
  private Boolean locked;

  @Builder.Default
  private Set<String> countryGroups = emptySet();

  public UpdateUserRequest toCommand(String username) {
    return UpdateUserRequest.builder()
        .username(username)
        .displayName(displayName)
        .roles(roles)
        .countryGroups(countryGroups)
        .locked(locked)
        .build();
  }
}
