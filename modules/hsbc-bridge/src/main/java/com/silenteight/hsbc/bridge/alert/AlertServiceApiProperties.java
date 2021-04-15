package com.silenteight.hsbc.bridge.alert;

import lombok.Value;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties("silenteight.bridge.alert.service.api")
@Value
class AlertServiceApiProperties {

  String grpcAddress;
  long deadlineInSeconds;
}
