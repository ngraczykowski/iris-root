package com.silenteight.hsbc.bridge.grpc;

import lombok.Value;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties("silenteight.bridge.dataset.service.api")
@Value
class DatasetGrpcAdapterProperties {

  String grpcAddress;
  long deadlineInSeconds;
}
