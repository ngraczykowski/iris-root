package com.silenteight.hsbc.bridge.grpc;

import lombok.Value;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties("silenteight.bridge.transfer.model.service.api")
@Value
class TransferModelGrpcAdapterProperties {

  String grpcAddress;
  long deadlineInSeconds;
}
