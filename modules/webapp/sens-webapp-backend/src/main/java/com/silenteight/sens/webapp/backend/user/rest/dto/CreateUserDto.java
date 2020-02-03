package com.silenteight.sens.webapp.backend.user.rest.dto;

import lombok.*;

import com.silenteight.sens.webapp.backend.user.registration.RegisterUserUseCase.RegisterUserCommand;

import java.util.Set;
import javax.validation.constraints.NotEmpty;

import static java.util.Collections.emptySet;

@Data
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
  @NotEmpty
  private String displayName;

  private Set<String> roles = emptySet();

  public RegisterUserCommand toCommand() {
    return RegisterUserCommand.builder()
        .username(getUserName())
        .password(getPassword())
        .displayName(getDisplayName())
        .roles(getRoles())
        .build();
  }
}
