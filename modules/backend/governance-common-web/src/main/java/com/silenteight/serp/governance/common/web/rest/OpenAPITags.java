package com.silenteight.serp.governance.common.web.rest;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class OpenAPITags {
  //TODO(dsniezek) should be merged with:
  //* com.silenteight.sens.webapp.backend.configuration.DomainConstants
  //* com.silenteight.serp.governance.file.domain.DomainConstants
  //* ...
  public static final String CONFIGURATION_ENDPOINT_TAG = "Configuration";
}
