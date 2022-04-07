package com.silenteight.connector.ftcc.ingest.adapter.outgoing.amqp;

import com.silenteight.connector.ftcc.ingest.domain.port.outgoing.DataPrepMessageGateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.gateway.GatewayProxyFactoryBean;

import static com.silenteight.connector.ftcc.ingest.adapter.outgoing.amqp.DataPrepChannels.DATA_PREP_GATEWAY_CHANNEL;

@Configuration
class DataPrepGatewayIntegrationConfiguration {

  @Bean
  GatewayProxyFactoryBean dataPrepMessageGateway() {
    GatewayProxyFactoryBean factoryBean = new GatewayProxyFactoryBean(DataPrepMessageGateway.class);
    factoryBean.setDefaultRequestChannel(new DirectChannel());
    factoryBean.setDefaultRequestChannelName(DATA_PREP_GATEWAY_CHANNEL);
    return factoryBean;
  }
}
