/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.sens.webapp.user.rest.dto;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import com.silenteight.sep.usermanagement.api.credentials.dto.TemporaryPassword;

@Value
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class TemporaryPasswordDto {

  String temporaryPassword;

  public static TemporaryPasswordDto from(TemporaryPassword tempPassword) {
    return new TemporaryPasswordDto(tempPassword.getPassword());
  }
}
