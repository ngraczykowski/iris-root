package com.silenteight.hsbc.bridge.model;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("grpc.governance")
class SolvingModelGrpcProperties {

  private String address;
}
