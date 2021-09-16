package com.silenteight.payments.bridge.app.security;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.Valid;

@ConfigurationProperties(prefix = "pb.security")
@Valid
@Data
class SecurityProperties {

  private boolean disableSecurity;
}
