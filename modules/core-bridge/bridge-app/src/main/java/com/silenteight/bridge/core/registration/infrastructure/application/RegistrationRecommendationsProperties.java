package com.silenteight.bridge.core.registration.infrastructure.application;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties("registration.recommendations")
public record RegistrationRecommendationsProperties(
    int recommendationsStoredUpdateAlertsChunkSize) {}
