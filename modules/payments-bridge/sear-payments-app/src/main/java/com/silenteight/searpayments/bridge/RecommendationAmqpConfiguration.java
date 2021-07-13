//package com.silenteight.searpaymentsmockup;
//
//import lombok.RequiredArgsConstructor;
//
//import com.silenteight.sep.base.common.messaging.AmqpInboundFactory;
//
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.integration.amqp.dsl.AmqpInboundChannelAdapterSMLCSpec;
//import org.springframework.integration.dsl.IntegrationFlow;
//
//import static org.springframework.integration.dsl.IntegrationFlows.from;
//
//@Configuration
//@RequiredArgsConstructor
//public class RecommendationAmqpConfiguration {
//
//  private final AmqpInboundFactory inboundFactory;
//
//  @Bean
//  IntegrationFlow agentResponseIntegrationFlow() {
//    return from(createInboundAdapter("ae.recommendations-generated"))
//        .channel("recommendationGeneratedChannel")
//        .get();
//  }
//
//  private AmqpInboundChannelAdapterSMLCSpec createInboundAdapter(String... queueNames) {
//    return inboundFactory
//        .simpleAdapter()
//        .configureContainer(c -> c.addQueueNames(queueNames));
//  }
//}
