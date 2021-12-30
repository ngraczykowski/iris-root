package com.silenteight.bridge.core.registration.infrastructure.grpc;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.time.Duration;

@ConstructorBinding
@ConfigurationProperties("silenteight.bridge.grpc.registration")
record GrpcConfigurationProperties(Duration governanceDeadline,
                                   Duration adjudicationEngineDeadline) {}
