package com.silenteight.adjudication.engine.app.security;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.Valid;

@ConfigurationProperties(prefix = "ae.security")
@Valid
@Data
class OAuthSecurityProperties {

  private boolean disableSecurity;
}
