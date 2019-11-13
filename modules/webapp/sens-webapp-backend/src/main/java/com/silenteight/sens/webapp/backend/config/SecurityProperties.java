package com.silenteight.sens.webapp.backend.config;

import lombok.Data;

import com.silenteight.sens.webapp.backend.config.CasProperties.Logout;

import org.springframework.boot.context.properties.NestedConfigurationProperty;

import javax.validation.Valid;

@Data
class SecurityProperties {

  @Valid
  @NestedConfigurationProperty
  private final CasProperties cas = new CasProperties();

  public String getLoginUrl() {
    return cas.getLoginUrl();
  }

  public String getLogoutUrl() {
    return cas.getLogoutUrl();
  }

  public String getProviderKey() {
    return cas.getProviderKey();
  }

  public String getTicketValidatorUrl() {
    return cas.getTicketValidatorUrl();
  }

  public String getServiceId() {
    return cas.getServiceId();
  }

  public Logout getLogout() {
    return cas.getLogout();
  }
}
