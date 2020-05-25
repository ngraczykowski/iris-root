package com.silenteight.serp.common.testing.messaging;

import lombok.Getter;

import com.silenteight.serp.common.messaging.MessageSenderFactory;

public class MessageSenderSpyFactory implements MessageSenderFactory {

  @Getter
  private MessageSenderSpy lastMessageSender;
  @Getter
  private String lastExchangeName;

  @Override
  public MessageSenderSpy get(String exchangeName) {
    lastExchangeName = exchangeName;
    lastMessageSender = new MessageSenderSpy();
    return lastMessageSender;
  }
}
