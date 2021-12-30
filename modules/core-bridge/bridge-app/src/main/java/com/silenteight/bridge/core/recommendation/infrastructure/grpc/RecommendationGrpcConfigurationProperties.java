package com.silenteight.bridge.core.recommendation.infrastructure.grpc;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.time.Duration;

@ConstructorBinding
@ConfigurationProperties("silenteight.bridge.grpc.recommendation")
record RecommendationGrpcConfigurationProperties(Duration recommendationDeadline) {}
