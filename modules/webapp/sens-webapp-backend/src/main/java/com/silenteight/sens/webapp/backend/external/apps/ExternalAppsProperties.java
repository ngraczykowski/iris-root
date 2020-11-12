package com.silenteight.sens.webapp.backend.external.apps;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;


@ConfigurationProperties(prefix = "sens.webapp.external.apps")
@RequiredArgsConstructor
@ConstructorBinding
@Getter
class ExternalAppsProperties {

  private final String reportingUrl;
}


