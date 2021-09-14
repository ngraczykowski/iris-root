package com.silenteight.payments.bridge.firco.callback;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@Validated
@ConfigurationProperties(prefix = "pb.cmapi.callback")
class CallbackRequestProperties {

  @Valid
  private String endpoint;

  @Valid
  @NotNull
  private boolean enabled;
}
