package com.silenteight.serp.governance.agent.domain.file.configuration;

import com.silenteight.serp.governance.agent.domain.file.details.NonResolvableResourceException;

import static java.lang.String.format;

public class UnreachableConfigurationException extends NonResolvableResourceException {

  private static final long serialVersionUID = -3921517430404079131L;

  public UnreachableConfigurationException(String configFile) {
    super(format(
        "Agent configuration details for the following file=%s, cannot be obtained",
        configFile));
  }
}
