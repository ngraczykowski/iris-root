package com.silenteight.adjudication.engine.analysis.agentexchange;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class IntegrationGatewaysConfiguration {
  //
  //  private final AmqpInboundFactory inboundFactory;
  //  private final AmqpOutboundFactory outboundFactory;
  //  private final RequestMissingFeatureValuesUseCase requestMissingFeatureValuesUseCase;
  //  static final String AGENT_REQUEST_OUTBOUND_CHANNEL = "agentRequestOutboundChannel";
  //  static final String SOLVE_GATEWAY_OUTBOUND_CHANNEL = "solveOutboundChannel";

  //  @Bean
  //  public GatewayProxyFactoryBean agentRequestGateway() {
  //    GatewayProxyFactoryBean factoryBean = new GatewayProxyFactoryBean(
  //        AgentExchangeRequestGateway.class);
  //    factoryBean.setDefaultRequestChannel(new DirectChannel());
  //    factoryBean.setDefaultRequestChannelName(AGENT_REQUEST_OUTBOUND_CHANNEL);
  //    return factoryBean;
  //  }
  //
  //  @Bean
  //  public GatewayProxyFactoryBean solveGateway() {
  //    GatewayProxyFactoryBean factoryBean = new GatewayProxyFactoryBean(SolveGateway.class);
  //    factoryBean.setDefaultRequestChannel(new DirectChannel());
  //    factoryBean.setDefaultRequestChannelName(SOLVE_GATEWAY_OUTBOUND_CHANNEL);
  //    return factoryBean;
  //  }
  //
  //  @Bean
  //  IntegrationFlow startSolveIntegrationFlow() {
  //    return IntegrationFlows
  //        .from(SOLVE_GATEWAY_OUTBOUND_CHANNEL)
  //        .handle(m -> requestMissingFeatureValuesUseCase.invoke())
  //        .get();
  //  }
  //
  //  @Bean
  //  IntegrationFlow agentRequestGatewayIntegrationFlow() {
  //    return from(AGENT_REQUEST_OUTBOUND_CHANNEL)
  //        .transform(new AgentExchangeEntityTransformer())
  //        .handle(outboundFactory
  //            .outboundAdapter()
  //            .exchangeName("agent.request")
  //            .routingKey("TODO"))
  //        .get();
  //  }
  //
  //  private static class AgentExchangeEntityTransformer
  //      implements GenericTransformer<AgentExchange, AgentExchangeRequest> {
  //
  //    @Override
  //    public AgentExchangeRequest transform(AgentExchange e) {
  //      AgentExchangeRequest r = AgentExchangeRequest
  //          .newBuilder()
  //          .addFeatures(e.getMatchFeatures().get(0).getFeature())
  //          .addAllMatches(e
  //              .getMatchFeatures()
  //              .stream()
  //              .map(AgentExchangeMatchFeature::getMatchId)
  //              .map(String::valueOf)
  //              .collect(Collectors.toList()))
  //          .build();
  //      return r;
  //    }
  //  }
  //
  //  @Bean
  //  IntegrationFlow alertOutboundIntegrationFlow() {
  //    return createOutboundFlow(
  //        AGENT_REQUEST_OUTBOUND_CHANNEL,
  //        properties.getAlert().getOutboundExchangeName(),
  //        properties.getAlert().getOutboundRoutingKey());
  //  }
  //
  //  private StandardIntegrationFlow createOutboundFlow(
  //      String outboundChannel,
  //      String outboundExchangeName,
  //      String outboundRoutingKey) {
  //
  //    return from(outboundChannel)
  //        .handle(createOutboundAdapter(outboundExchangeName, outboundRoutingKey))
  //        .get();
  //  }
  //
  //  private AmqpBaseOutboundEndpointSpec createOutboundAdapter(
  //      String outboundExchangeName, String outboundRoutingKey) {
  //
  //    return outboundFactory
  //        .outboundAdapter()
  //        .exchangeName(outboundExchangeName)
  //        .routingKeyExpression("TODO");
  //  }
  //
  //  private ApplicationListener<?> applicationListener() {
  //    ApplicationEventListeningMessageProducer producer =
  //        new ApplicationEventListeningMessageProducer();
  //    producer.setEventTypes(SolveRequestedEvent.class);
  //    producer.setOutputChannel(resultsChannel());
  //    return producer;
  //  }
  //
  //  private IntegrationFlow integrationFlow() {
  //    return IntegrationFlows.from(applicationListener()).handle()
  //  }
}
