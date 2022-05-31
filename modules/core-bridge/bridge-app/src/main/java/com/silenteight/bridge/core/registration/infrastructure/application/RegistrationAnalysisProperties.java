package com.silenteight.bridge.core.registration.infrastructure.application;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.time.Duration;

@ConstructorBinding
@ConfigurationProperties("registration.analysis")
public record RegistrationAnalysisProperties(Duration alertTtl,
                                             Boolean mockRecommendationsGeneration) {}
