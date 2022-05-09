package com.silenteight.sep.usermanagement.api.credentials;

import com.silenteight.sep.usermanagement.api.credentials.dto.TemporaryPassword;

public interface TemporaryPasswordGenerator {

  TemporaryPassword generate();
}
