package com.silenteight.adjudication.engine.analysis.service.integration;

import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.common.messaging.AmqpInboundFactory;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AnalysisInboundAmqpIntegrationProperties.class)
@RequiredArgsConstructor
class AnalysisInboundAmqpIntegrationConfiguration {

  private final AmqpInboundFactory inboundFactory;
  private final AnalysisInboundAmqpIntegrationProperties properties;
}
