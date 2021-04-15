package com.silenteight.hsbc.bridge.model;

import lombok.Value;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties("silenteight.bridge.solving.model.service.api")
@Value
class SolvingModelGrpcProperties {

  String grpcAddress;
  long deadlineInSeconds;
}
