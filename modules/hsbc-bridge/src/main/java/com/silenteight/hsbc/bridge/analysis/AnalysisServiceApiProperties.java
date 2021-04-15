package com.silenteight.hsbc.bridge.analysis;

import lombok.Value;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties("silenteight.bridge.analysis.service.api")
@Value
class AnalysisServiceApiProperties {

  String grpcAddress;
  long deadlineInSeconds;
}
