package com.silenteight.hsbc.bridge.model;

import lombok.Getter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Component
@ConfigurationProperties("grpc.governance")
class SolvingModelGrpcProperties {

  private String address;
}
