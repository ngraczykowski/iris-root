package com.silenteight.hsbc.bridge.grpc;

import lombok.Value;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties("silenteight.bridge.solving.model.service.api")
@Value
class ModelGrpcAdapterProperties {

  String grpcAddress;
  long deadlineInSeconds;
}
