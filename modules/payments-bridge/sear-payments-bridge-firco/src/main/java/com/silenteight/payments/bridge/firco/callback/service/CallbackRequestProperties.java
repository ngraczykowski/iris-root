package com.silenteight.payments.bridge.firco.callback.service;

import lombok.Data;

import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties.Registration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@ConfigurationProperties(prefix = "pb.cmapi.callback")
class CallbackRequestProperties {

  private String endpoint;

  private boolean enabled;

  /**
   * The name of the client registration in OAuth2 client configuration.
   * <p/>
   * For more information, see {@link Registration}.
   */
  private String clientRegistrationId;
}
