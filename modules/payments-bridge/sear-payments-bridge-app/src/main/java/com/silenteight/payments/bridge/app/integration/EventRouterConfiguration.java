package com.silenteight.payments.bridge.app.integration;

import com.silenteight.payments.bridge.event.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.router.PayloadTypeRouter;
import org.springframework.messaging.MessageChannel;

import static com.silenteight.payments.bridge.app.integration.EventChannels.*;

@Configuration
class EventRouterConfiguration {

  static final String EVENT_GATEWAY_CHANNEL = "eventChannel";

  @ServiceActivator(inputChannel = EVENT_GATEWAY_CHANNEL)
  @Bean
  PayloadTypeRouter router() {
    PayloadTypeRouter router = new PayloadTypeRouter();
    router.setChannelMapping(AlertStoredEvent.class.getName(), AlertStoredEvent.CHANNEL);
    router.setChannelMapping(AlertInitializedEvent.class.getName(), AlertInitializedEvent.CHANNEL);
    router.setChannelMapping(AlertRegisteredEvent.class.getName(), AlertRegisteredEvent.CHANNEL);
    router.setChannelMapping(
        AlertInputAcceptedEvent.class.getName(), AlertInputAcceptedEvent.CHANNEL);
    router.setChannelMapping(
        AlertAddedToAnalysisEvent.class.getName(), AlertAddedToAnalysisEvent.CHANNEL);
    router.setChannelMapping(AlertRejectedEvent.class.getName(), AlertRejectedEvent.CHANNEL);
    router.setChannelMapping(AlertDeliveredEvent.class.getName(), AlertDeliveredEvent.CHANNEL);
    router.setChannelMapping(AlertUndeliveredEvent.class.getName(), AlertUndeliveredEvent.CHANNEL);
    router.setChannelMapping(
        RecommendationGeneratedEvent.class.getName(), RecommendationGeneratedEvent.CHANNEL);
    router.setChannelMapping(
        RecommendationCompletedEvent.class.getName(), RecommendationCompletedEvent.CHANNEL);
    router.setChannelMapping(ModelUpdatedEvent.class.getName(), ModelUpdatedEvent.CHANNEL);
    return router;
  }

  @Bean(EVENT_GATEWAY_CHANNEL)
  MessageChannel messageChannel() {
    return ChannelFactory.createDirectChannel();
  }

}
