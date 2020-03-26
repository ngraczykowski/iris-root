package com.silenteight.sens.webapp.backend.user.rest.dto;

import lombok.*;

import com.silenteight.sens.webapp.user.registration.RegisterInternalUserUseCase.RegisterInternalUserCommand;

import java.util.Set;
import javax.validation.constraints.NotEmpty;

import static java.util.Collections.emptySet;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateUserDto {

  @NonNull
  @NotEmpty
  private String userName;

  @NonNull
  @NotEmpty
  @ToString.Exclude
  private String password;

  @NonNull
  private String displayName;

  @Builder.Default
  private Set<String> roles = emptySet();

  public RegisterInternalUserCommand toCommand() {
    return RegisterInternalUserCommand.builder()
        .username(getUserName())
        .password(getPassword())
        .displayName(getDisplayName())
        .roles(getRoles())
        .build();
  }
}
