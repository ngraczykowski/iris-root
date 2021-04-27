package com.silenteight.hsbc.bridge.grpc;

import lombok.Value;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties("silenteight.bridge.alert.service.api")
@Value
class AlertGrpcAdapterProperties {

  String grpcAddress;
  long deadlineInSeconds;
}
