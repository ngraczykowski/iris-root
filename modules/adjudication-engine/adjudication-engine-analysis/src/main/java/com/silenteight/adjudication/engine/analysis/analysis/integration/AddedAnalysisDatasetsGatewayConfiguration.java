package com.silenteight.adjudication.engine.analysis.analysis.integration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.gateway.GatewayProxyFactoryBean;

import static com.silenteight.adjudication.engine.analysis.analysis.integration.AnalysisChannels.ADDED_ANALYSIS_ALERTS_OUTBOUND_CHANNEL;

@Configuration
class AddedAnalysisDatasetsGatewayConfiguration {

  @Bean
  GatewayProxyFactoryBean addedAnalysisAlertsGateway() {
    var factoryBean = new GatewayProxyFactoryBean(AddedAnalysisAlertsGateway.class);
    factoryBean.setDefaultRequestChannel(new DirectChannel());
    factoryBean.setDefaultRequestChannelName(ADDED_ANALYSIS_ALERTS_OUTBOUND_CHANNEL);
    return factoryBean;
  }
}
