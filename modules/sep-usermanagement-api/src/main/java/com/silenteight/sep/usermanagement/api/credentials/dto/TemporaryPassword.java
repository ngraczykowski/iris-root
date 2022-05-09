package com.silenteight.sep.usermanagement.api.credentials.dto;

import lombok.Value;

@Value(staticConstructor = "of")
public class TemporaryPassword {

  String password;
}
