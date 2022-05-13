package com.silenteight.universaldatasource.app.security;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.Valid;

@ConfigurationProperties(prefix = "uds.security")
@Valid
@Data
class SecurityProperties {

  private boolean disableSecurity;
}
