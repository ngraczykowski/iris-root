package com.silenteight.serp.common.messaging;

import lombok.RequiredArgsConstructor;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;

import java.util.Collection;

@RequiredArgsConstructor
public class ListeningMessagePostProcessor implements MessagePostProcessor {

  private final Collection<? extends ReceiveMessageListener> listeners;

  @Override
  public Message postProcessMessage(Message message) {
    onReceived(message);
    return message;
  }

  private void onReceived(Message message) {
    listeners.forEach(listener -> listener.onReceived(message));
  }
}
