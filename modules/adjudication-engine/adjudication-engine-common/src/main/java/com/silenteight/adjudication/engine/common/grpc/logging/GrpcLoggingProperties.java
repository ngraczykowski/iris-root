package com.silenteight.adjudication.engine.common.grpc.logging;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "grpc.logging")
class GrpcLoggingProperties {

  private final boolean enabled = false;
}
