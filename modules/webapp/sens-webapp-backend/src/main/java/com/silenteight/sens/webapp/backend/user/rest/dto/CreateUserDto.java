package com.silenteight.sens.webapp.backend.user.rest.dto;

import lombok.*;

import com.silenteight.sens.webapp.user.registration.RegisterInternalUserUseCase.RegisterInternalUserCommand;

import java.util.Set;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import static com.silenteight.sens.webapp.backend.user.rest.DomainConstants.USER_FIELD_MAX_LENGTH;
import static com.silenteight.sens.webapp.backend.user.rest.DomainConstants.USER_FIELD_MIN_LENGTH;
import static java.util.Collections.emptySet;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateUserDto {

  @NonNull
  @NotEmpty
  @Size(min = USER_FIELD_MIN_LENGTH, max = USER_FIELD_MAX_LENGTH)
  private String userName;

  @NonNull
  @NotEmpty
  @ToString.Exclude
  private String password;

  @NonNull
  @Size(min = USER_FIELD_MIN_LENGTH, max = USER_FIELD_MAX_LENGTH)
  private String displayName;

  @Builder.Default
  private Set<String> roles = emptySet();

  @Builder.Default
  private Set<String> countryGroups = emptySet();

  public RegisterInternalUserCommand toCommand() {
    return RegisterInternalUserCommand.builder()
        .username(getUserName())
        .password(getPassword())
        .displayName(getDisplayName())
        .roles(getRoles())
        .countryGroups(getCountryGroups())
        .build();
  }
}
