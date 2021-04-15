package com.silenteight.hsbc.bridge.adjudication;

import lombok.Value;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties("silenteight.bridge.dataset.service.api")
@Value
class DatasetServiceApiProperties {

  String grpcAddress;
  long deadlineInSeconds;
}
