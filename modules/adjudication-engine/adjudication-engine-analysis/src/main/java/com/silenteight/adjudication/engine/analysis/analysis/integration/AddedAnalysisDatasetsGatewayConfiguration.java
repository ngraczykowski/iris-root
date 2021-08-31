package com.silenteight.adjudication.engine.analysis.analysis.integration;

import com.silenteight.adjudication.engine.analysis.analysis.AnalysisAlertsAddedGateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.gateway.GatewayProxyFactoryBean;
import org.springframework.messaging.MessageChannel;

import static com.silenteight.adjudication.engine.analysis.analysis.integration.AnalysisChannels.ANALYSIS_ALERTS_ADDED_OUTBOUND_CHANNEL;

@Configuration
class AddedAnalysisDatasetsGatewayConfiguration {

  @Bean
  MessageChannel analysisAlertsAddedOutboundChannel() {
    return MessageChannels.direct(ANALYSIS_ALERTS_ADDED_OUTBOUND_CHANNEL).get();
  }

  @Bean
  GatewayProxyFactoryBean addedAnalysisAlertsGateway() {
    var factoryBean = new GatewayProxyFactoryBean(AnalysisAlertsAddedGateway.class);
    factoryBean.setDefaultRequestChannel(analysisAlertsAddedOutboundChannel());
    return factoryBean;
  }
}
