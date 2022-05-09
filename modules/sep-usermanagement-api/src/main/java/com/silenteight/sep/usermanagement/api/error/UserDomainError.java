package com.silenteight.sep.usermanagement.api.error;

import java.io.Serializable;

public interface UserDomainError extends Serializable {

  String getReason();
}
