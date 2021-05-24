package com.silenteight.hsbc.datasource.grpc;

import lombok.Value;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties("silenteight.datasource.worldcheck.service.api")
@Value
class NameInformationGrpcAdapterProperties {

  String grpcAddress;
  long deadlineInSeconds;
}
