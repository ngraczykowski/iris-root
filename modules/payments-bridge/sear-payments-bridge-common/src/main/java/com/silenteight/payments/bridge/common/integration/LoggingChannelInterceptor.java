package com.silenteight.payments.bridge.common.integration;

import lombok.extern.slf4j.Slf4j;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;

@Slf4j
public class LoggingChannelInterceptor implements ChannelInterceptor {

  @Override
  public Message<?> postReceive(Message<?> message, MessageChannel channel) {
    log.debug("Channel [{}] received message [{}]", channel, message);
    return message;
  }

  @Override
  public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
    log.debug("Channel [{}] sent message [{}] status: {}", channel, message, sent);
  }
}
