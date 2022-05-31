package com.silenteight.bridge.core.registration.infrastructure.application;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({
    RegistrationAnalysisProperties.class, RegistrationRecommendationsProperties.class })
public class RegistrationConfiguration {
}
