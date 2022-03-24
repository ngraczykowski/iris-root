package com.silenteight.scb.outputrecommendation.infrastructure.grpc;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.time.Duration;

@ConstructorBinding
@ConfigurationProperties("silenteight.scb-bridge.grpc.recommendation")
record RecommendationGrpcConfigurationProperties(Duration recommendationDeadline) {}
