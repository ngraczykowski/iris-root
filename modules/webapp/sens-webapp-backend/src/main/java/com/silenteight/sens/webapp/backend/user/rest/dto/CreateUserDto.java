package com.silenteight.sens.webapp.backend.user.rest.dto;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

import com.silenteight.sens.webapp.user.registration.RegisterInternalUserUseCase.RegisterInternalUserCommand;
import com.silenteight.serp.governance.common.web.rest.RestValidationConstants;

import java.util.Set;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static com.silenteight.sens.webapp.backend.user.rest.DomainConstants.USER_FIELD_MAX_LENGTH;
import static com.silenteight.sens.webapp.backend.user.rest.DomainConstants.USER_FIELD_MIN_LENGTH;
import static java.util.Collections.emptySet;

@Data
@Builder
@Jacksonized
public class CreateUserDto {

  @NonNull
  @NotEmpty
  @Size(min = USER_FIELD_MIN_LENGTH, max = USER_FIELD_MAX_LENGTH)
  @Pattern(regexp = RestValidationConstants.FIELD_REGEX)
  private String userName;

  @NonNull
  @NotEmpty
  @ToString.Exclude
  private String password;

  @NonNull
  @Pattern(regexp = RestValidationConstants.FIELD_REGEX)
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
