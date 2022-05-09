package com.silenteight.sep.usermanagement.api.configuration;

import com.silenteight.sep.usermanagement.api.configuration.dto.AuthConfigurationDto;

public interface ConfigurationQuery {

  int getSessionIdleTimeoutSeconds();

  AuthConfigurationDto getAuthConfiguration();
}
