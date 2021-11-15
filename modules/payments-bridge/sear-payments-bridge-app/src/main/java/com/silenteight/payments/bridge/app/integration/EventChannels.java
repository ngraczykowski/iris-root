package com.silenteight.payments.bridge.app.integration;

import com.silenteight.payments.bridge.event.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.SubscribableChannel;

@Configuration
class EventChannels {

  @Bean(AlertStoredEvent.CHANNEL)
  SubscribableChannel alertStored() {
    return ChannelFactory.createPublishSubscribeChannel(true);
  }

  @Bean(AlertInitializedEvent.CHANNEL)
  SubscribableChannel alertInitialized() {
    return ChannelFactory.createPublishSubscribeChannel(true);
  }

  @Bean(AlertUndeliveredEvent.CHANNEL)
  SubscribableChannel undeliveredAlertChannel() {
    return ChannelFactory.createPublishSubscribeChannel(true);
  }

  @Bean(AlertDeliveredEvent.CHANNEL)
  SubscribableChannel deliveredAlertChannel() {
    return ChannelFactory.createPublishSubscribeChannel(true);
  }

  @Bean(AlertRegisteredEvent.CHANNEL)
  SubscribableChannel alertRegistered() {
    return ChannelFactory.createPublishSubscribeChannel(true);
  }

  @Bean(AlertInputAcceptedEvent.CHANNEL)
  SubscribableChannel alertInputAccepted() {
    return ChannelFactory.createPublishSubscribeChannel(true);
  }

  @Bean(AlertAddedToAnalysisEvent.CHANNEL)
  SubscribableChannel alertAddedToAnalysis() {
    return ChannelFactory.createPublishSubscribeChannel(true);
  }

  @Bean(AlertRejectedEvent.CHANNEL)
  SubscribableChannel alertRejected() {
    return ChannelFactory.createPublishSubscribeChannel(false);
  }

  @Bean(RecommendationGeneratedEvent.CHANNEL)
  SubscribableChannel recommendationGenerated() {
    return ChannelFactory.createPublishSubscribeChannel(true);
  }

  @Bean(RecommendationCompletedEvent.CHANNEL)
  SubscribableChannel recommendationCompleted() {
    return ChannelFactory.createPublishSubscribeChannel(true);
  }

  @Bean(ModelUpdatedEvent.CHANNEL)
  SubscribableChannel solvingModelUpdated() {
    return ChannelFactory.createPublishSubscribeChannel(true);
  }

}
