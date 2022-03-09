package com.silenteight.scb.ingest.infrastructure.grpc;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.time.Duration;

@ConstructorBinding
@ConfigurationProperties("silenteight.scb-bridge.grpc.registration")
record RegistrationGrpcConfigurationProperties(Duration registrationDeadline) {}
