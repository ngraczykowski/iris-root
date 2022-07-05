/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.sens.webapp.user.rest.dto;

import lombok.*;

import com.silenteight.sens.webapp.user.update.UpdateUserUseCase.UpdateUserRequest;
import com.silenteight.serp.governance.common.web.rest.RestValidationConstants;

import java.util.Set;
import javax.annotation.Nullable;
import javax.validation.constraints.Pattern;

import static java.util.Collections.emptySet;

@Builder
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UpdateUserDto {

  @Nullable
  @Pattern(regexp = RestValidationConstants.FIELD_REGEX)
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
