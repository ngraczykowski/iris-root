package com.silenteight.hsbc.bridge.adjudication;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("grpc.adjudication")
class AdjudicationProperties {

  private String address;
}
