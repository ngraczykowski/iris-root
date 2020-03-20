package com.silenteight.sens.webapp.user.password;

import lombok.Value;

@Value(staticConstructor = "of")
public class TemporaryPassword {

  String password;
}
