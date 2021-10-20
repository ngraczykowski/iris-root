package com.silenteight.payments.bridge.common.integration;

import com.silenteight.payments.bridge.event.*;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;
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

  // AMQP outbound channels
  public static final String MESSAGE_STORED_OUTBOUND = PREFIX + "MessageStoredOutboundChannel";
  public static final String RESPONSE_COMPLETED_OUTBOUND =
      PREFIX + "ResponseCompletedOutboundChannel";
  public static final String WAREHOUSE_UPDATE_OUTBOUND = PREFIX + "WarehouseOutboundChannel";

  // Domain Events channels
  public static final String ALERT_STORED = PREFIX + "AlertStoredChannel";
  public static final String ALERT_INITIALIZED = PREFIX + "AlertInitializedChannel";
  public static final String ALERT_REGISTERED = PREFIX + "AlertRegisteredChannel";
  public static final String ALERT_INPUT_ACCEPTED = PREFIX + "AlertInputAcceptedChannel";
  public static final String ALERT_ADDED_TO_ANALYSIS = PREFIX + "AlertAddedToAnalysisChannel";

  public static final String ALERT_REJECTED = PREFIX + "AlertRejectedChannel";
  public static final String ALERT_DELIVERED = PREFIX + "AlertDeliveredChannel";
  public static final String ALERT_UNDELIVERED = PREFIX + "AlertUndeliveredChannel";

  public static final String RECOMMENDATION_GENERATED = PREFIX + "RecommendationGeneratedChannel";
  public static final String RECOMMENDATION_COMPLETED = PREFIX + "RecommendationCompletedChannel";

  // Integration
  public static final String ALERT_REGISTERED_REQUEST_CHANNEL =
      PREFIX + "AlertRegisteredRequestChannel";
  public static final String ALERT_REGISTERED_RESPONSE_CHANNEL =
      PREFIX + "AlertRegisteredResponseChannel";


  @Bean(MESSAGE_STORED_OUTBOUND)
  public MessageChannel messageStoredOutbound() {
    return new DirectChannel();
  }

  @Bean(RESPONSE_COMPLETED_OUTBOUND)
  public MessageChannel responseCompletedOutbound() {
    return new DirectChannel();
  }

  @Bean(WAREHOUSE_UPDATE_OUTBOUND)
  public MessageChannel warehouseOutbound() {
    var directChannel = new DirectChannel();
    directChannel.addInterceptor(new LoggingChannelInterceptor());
    return directChannel;
  }

  @Bean(ALERT_STORED)
  public SubscribableChannel alertStored() {
    return new TypedPublishSubscribeChannel(AlertStoredEvent.class, channelInterceptors);
  }

  @Bean(ALERT_INITIALIZED)
  public SubscribableChannel alertInitialized() {
    return new TypedPublishSubscribeChannel(AlertInitializedEvent.class, channelInterceptors);
  }

  @Bean(ALERT_UNDELIVERED)
  public SubscribableChannel undeliveredAlertChannel() {
    return new TypedPublishSubscribeChannel(AlertUndeliveredEvent.class, channelInterceptors);
  }

  @Bean(ALERT_DELIVERED)
  public SubscribableChannel deliveredAlertChannel() {
    return new TypedPublishSubscribeChannel(AlertDeliveredEvent.class, channelInterceptors);
  }

  @Bean(ALERT_REGISTERED)
  public SubscribableChannel alertRegistered() {
    return new TypedPublishSubscribeChannel(AlertRegisteredEvent.class, channelInterceptors);
  }

  @Bean(ALERT_INPUT_ACCEPTED)
  public SubscribableChannel alertInputAccepted() {
    return new TypedPublishSubscribeChannel(AlertInputAcceptedEvent.class, channelInterceptors);
  }

  @Bean(ALERT_ADDED_TO_ANALYSIS)
  public SubscribableChannel alertAddedToAnalysis() {
    return new TypedPublishSubscribeChannel(AlertAddedToAnalysisEvent.class, channelInterceptors);
  }

  @Bean(ALERT_REJECTED)
  public SubscribableChannel alertRejected() {
    return new TypedPublishSubscribeChannel(AlertRejectedEvent.class, channelInterceptors);
  }

  @Bean(RECOMMENDATION_GENERATED)
  public SubscribableChannel recommendationGenerated() {
    return new TypedPublishSubscribeChannel(
        RecommendationGeneratedEvent.class, channelInterceptors);
  }

  @Bean(RECOMMENDATION_COMPLETED)
  public SubscribableChannel recommendationCompleted() {
    return new TypedPublishSubscribeChannel(
        RecommendationCompletedEvent.class, channelInterceptors);
  }

  // Integration
  @Bean(ALERT_REGISTERED_REQUEST_CHANNEL)
  public MessageChannel alertRegisteredRequestChannel() {
    return new DirectChannel();
  }

  @Bean(ALERT_REGISTERED_RESPONSE_CHANNEL)
  public MessageChannel alertRegisteredResponseChannel() {
    return new DirectChannel();
  }

}
