package com.silenteight.payments.bridge.common.integration;

import com.silenteight.payments.bridge.event.*;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;

import java.util.List;

@Configuration
public class CommonChannels {

  public static final String CHANNEL_INTERCEPTOR_QUALIFIER = "FircoChannelInterceptors";

  private final List<ChannelInterceptor> channelInterceptors;

  public CommonChannels(
      @Qualifier(CHANNEL_INTERCEPTOR_QUALIFIER) List<ChannelInterceptor> channelInterceptors) {
    this.channelInterceptors = channelInterceptors;
  }

  private static final String PREFIX = "common";

  public static final String AMQP_OUTBOUND = "AMQPOutboundChannel";
  public static final String CMAPI_OUTBOUND = "CMApiOutboundChannel";

  public static final String ALERT_DELIVERED = PREFIX + "AlertDeliveredChannel";
  public static final String ALERT_REGISTERED = PREFIX + "AlertRegisteredChannel";
  public static final String ALERT_INPUT_ACCEPTED = PREFIX + "AlertInputAcceptedChannel";
  public static final String ALERT_REJECTED = PREFIX + "AlertRejectedChannel";
  public static final String RECOMMENDATION_RECEIVED = PREFIX + "RecommendationReceivedChannel";

  @Bean(AMQP_OUTBOUND)
  public MessageChannel amqpOutbound() {
    return new DirectChannel();
  }

  @Bean(CMAPI_OUTBOUND)
  public MessageChannel cmapiOutbound() {
    return new DirectChannel();
  }

  @Bean(ALERT_DELIVERED)
  public MessageChannel alertDelivered() {
    return new TypedPublishSubscribeChannel(AlertDelivered.class, channelInterceptors);
  }

  @Bean(ALERT_REGISTERED)
  public MessageChannel alertRegistered() {
    return new TypedPublishSubscribeChannel(AlertRegistered.class, channelInterceptors);
  }

  @Bean(ALERT_INPUT_ACCEPTED)
  public MessageChannel alertInputAccepted() {
    return new TypedPublishSubscribeChannel(AlertInputAccepted.class, channelInterceptors);
  }

  @Bean(ALERT_REJECTED)
  public MessageChannel alertRejected() {
    return new TypedPublishSubscribeChannel(AlertRejected.class, channelInterceptors);
  }

  @Bean(RECOMMENDATION_RECEIVED)
  public MessageChannel recommendationReceived() {
    return new TypedPublishSubscribeChannel(RecommendationReceived.class, channelInterceptors);
  }

}
