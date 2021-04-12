package com.silenteight.hsbc.bridge.alert;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("silenteight.bridge.alert.service.api")
class AlertServiceApiProperties {

  private String grpcAddress;
}
