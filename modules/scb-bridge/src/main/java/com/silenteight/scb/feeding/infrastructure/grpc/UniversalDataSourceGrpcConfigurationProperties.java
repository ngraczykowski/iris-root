package com.silenteight.scb.feeding.infrastructure.grpc;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.time.Duration;

@ConstructorBinding
@ConfigurationProperties("silenteight.scb-bridge.grpc.universal-data-source")
record UniversalDataSourceGrpcConfigurationProperties(Duration udsDeadline) {}
