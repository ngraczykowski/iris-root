package com.silenteight.sens.webapp.user.password.reset;

import lombok.Value;

@Value(staticConstructor = "of")
public class TemporaryPassword {

  String password;
}
