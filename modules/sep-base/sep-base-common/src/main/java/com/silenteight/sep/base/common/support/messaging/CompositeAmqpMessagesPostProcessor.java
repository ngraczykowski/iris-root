package com.silenteight.sep.base.common.support.messaging;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;

import java.util.List;

@RequiredArgsConstructor
public class CompositeAmqpMessagesPostProcessor implements MessagePostProcessor {

  @NonNull
  private final List<MessagePostProcessor> postProcessors;

  @Override
  public Message postProcessMessage(Message message) {
    Message currentMessage = message;
    for (MessagePostProcessor postProcessor : postProcessors) {
      currentMessage = postProcessor.postProcessMessage(currentMessage);
    }

    return currentMessage;
  }
}
