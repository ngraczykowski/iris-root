package com.silenteight.sens.webapp.backend.config;

import lombok.Data;

import org.hibernate.validator.constraints.NotBlank;
import org.jasig.cas.client.configuration.ConfigurationKeys;

import javax.validation.Valid;

@Data
public class CasProperties {

  @NotBlank
  private String loginUrl;

  @NotBlank
  private String logoutUrl;

  @NotBlank
  private String ticketValidatorUrl;

  @NotBlank
  private String serviceId;

  @NotBlank
  private String providerKey;

  @Valid
  private Logout logout = new Logout();

  @Data
  public static class Logout {

    private String artifactParameterName =
        ConfigurationKeys.ARTIFACT_PARAMETER_NAME.getDefaultValue();

    private String logoutParameterName = ConfigurationKeys.LOGOUT_PARAMETER_NAME.getDefaultValue();

    private String relayStateParameterName = ConfigurationKeys.RELAY_STATE_PARAMETER_NAME
        .getDefaultValue();

    private String casServerUrlPrefix = ConfigurationKeys.CAS_SERVER_URL_PREFIX.getDefaultValue();

    private boolean artifactParameterOverPost = ConfigurationKeys.ARTIFACT_PARAMETER_OVER_POST
        .getDefaultValue();

    private boolean eagerlyCreateSessions = ConfigurationKeys.EAGERLY_CREATE_SESSIONS
        .getDefaultValue();
  }
}
