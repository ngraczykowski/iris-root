package com.silenteight.hsbc.bridge.analysis;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("silenteight.bridge.analysis.service.api")
class AnalysisServiceApiProperties {

  private String grpcAddress;
}
