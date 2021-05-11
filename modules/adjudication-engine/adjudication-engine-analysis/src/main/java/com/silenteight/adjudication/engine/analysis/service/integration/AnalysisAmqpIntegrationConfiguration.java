package com.silenteight.adjudication.engine.analysis.service.integration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AnalysisAmqpIntegrationProperties.class)
class AnalysisAmqpIntegrationConfiguration {
}
