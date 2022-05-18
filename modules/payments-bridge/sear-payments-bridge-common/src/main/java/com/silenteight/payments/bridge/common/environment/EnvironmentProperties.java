package com.silenteight.payments.bridge.common.environment;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@ConfigurationProperties(prefix = "environment")
public class EnvironmentProperties {

  private String name;

}
