package com.silenteight.payments.bridge.firco.core.alertmessage.integration;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.firco.core.alertmessage.port.AlertMessageStoredPublisherPort;
import com.silenteight.payments.bridge.firco.core.alertmessage.port.ResponsePublisherPort;

import org.springframework.integration.gateway.GatewayProxyFactoryBean;

@RequiredArgsConstructor
//@Configuration
class AlertMessageIntegrationConfiguration {

  //  @Bean
  GatewayProxyFactoryBean alertMessageStoredRequest() {
    var factoryBean = new GatewayProxyFactoryBean(AlertMessageStoredPublisherPort.class);
    factoryBean.setDefaultRequestChannelName(
        AlertMessageChannels.ALERT_MESSAGE_STORED_REQUEST_CHANNEL);
    return factoryBean;
  }

  //  @Bean
  GatewayProxyFactoryBean alertMessageResponse() {
    var factoryBean = new GatewayProxyFactoryBean(ResponsePublisherPort.class);
    factoryBean.setDefaultRequestChannelName(
        AlertMessageChannels.ALERT_MESSAGE_RESPONSE_CHANNEL);
    return factoryBean;
  }
}
