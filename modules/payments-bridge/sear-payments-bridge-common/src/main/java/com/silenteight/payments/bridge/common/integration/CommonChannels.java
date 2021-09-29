package com.silenteight.payments.bridge.common.integration;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.common.integration.channel.AlertDeliveredChannel;
import com.silenteight.payments.bridge.common.integration.channel.AlertRegisteredChannel;
import com.silenteight.payments.bridge.event.AlertInputAccepted;
import com.silenteight.payments.bridge.event.AlertRejected;
import com.silenteight.payments.bridge.event.AlertStored;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.MessageChannel;

@Configuration
@RequiredArgsConstructor
public class CommonChannels {

  private final AlertDeliveredChannel alertDeliveryChannel;
  private final AlertRegisteredChannel alertRegisteredChannel;

  private static final String PREFIX = "common";

  public static final String AMQP_OUTBOUND = "AMQPOutboundChannel";

  public static final String ALERT_STORED = PREFIX + "AlertStoredChannel";
  public static final String ALERT_DELIVERED = PREFIX + "AlertDeliveredChannel";
  public static final String ALERT_REGISTERED = PREFIX + "AlertRegisteredChannel";
  public static final String ALERT_INPUT_ACCEPTED = PREFIX + "AlertInputAcceptedChannel";
  public static final String ALERT_REJECTED = PREFIX + "AlertRejectedChannel";

  @Bean(AMQP_OUTBOUND)
  public MessageChannel amqpOutbound() {
    return new DirectChannel();
  }

  @Bean(ALERT_STORED)
  public MessageChannel alertStored() {
    return new TypedPublishSubscribeChannel(AlertStored.class);
  }

  @Bean(ALERT_DELIVERED)
  public MessageChannel alertDelivered() {
    return alertDeliveryChannel.getChannel();
  }

  @Bean(ALERT_REGISTERED)
  public MessageChannel alertRegistered() {
    return alertRegisteredChannel.getChannel();
  }

  @Bean(ALERT_INPUT_ACCEPTED)
  public MessageChannel alertInputAccepted() {
    return new TypedPublishSubscribeChannel(AlertInputAccepted.class);
  }

  @Bean(ALERT_REJECTED)
  public MessageChannel alertRejected() {
    return new TypedPublishSubscribeChannel(AlertRejected.class);
  }

}
