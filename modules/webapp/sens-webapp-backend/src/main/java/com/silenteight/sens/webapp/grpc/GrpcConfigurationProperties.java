package com.silenteight.sens.webapp.grpc;

import lombok.Value;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.time.Duration;

@Value
@ConfigurationProperties("sens.grpc")
@ConstructorBinding
public class GrpcConfigurationProperties implements GrpcTimeoutConfig {

  Duration timeout;
}
