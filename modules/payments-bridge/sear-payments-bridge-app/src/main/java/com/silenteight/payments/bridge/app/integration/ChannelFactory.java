package com.silenteight.payments.bridge.app.integration;

import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.messaging.support.ChannelInterceptor;

public class ChannelFactory {

  private static final ChannelInterceptor LOGGING_INTERCEPTOR = new LoggingChannelInterceptor();

  public static PublishSubscribeChannel createPublishSubscribeChannel(boolean requireSubscribers) {
    var channel = new PublishSubscribeChannel(requireSubscribers);
    channel.addInterceptor(LOGGING_INTERCEPTOR);
    return channel;
  }

  public static DirectChannel createDirectChannel() {
    var channel = new DirectChannel();
    channel.addInterceptor(LOGGING_INTERCEPTOR);
    return channel;
  }
}
