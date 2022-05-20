package com.silenteight.sep.base.testing.messaging;

import lombok.Getter;

import com.silenteight.sep.base.common.messaging.MessageSender;
import com.silenteight.sep.base.common.messaging.properties.MessagePropertiesProvider;

import com.google.protobuf.Message;
import org.springframework.amqp.core.Address;
import org.springframework.amqp.core.MessageProperties;

import java.util.Optional;
import javax.annotation.Nullable;

@Getter
public class MessageSenderSpy implements MessageSender {

  @Nullable
  private String routingKeyUsed;
  @Nullable
  private Message sentMessage;
  @Nullable
  private MessageProperties messagePropertiesReceived;

  @Override
  public void send(Message protoMessage) {
    sentMessage = protoMessage;
  }

  @Override
  public void send(Message protoMessage, MessagePropertiesProvider propertiesProvider) {
    messagePropertiesReceived = propertiesProvider.get();
    send(protoMessage);
  }

  @Override
  public void send(String routingKey, Message protoMessage) {
    routingKeyUsed = routingKey;
    send(protoMessage);
  }

  @Override
  public void send(
      String routingKey,
      Message protoMessage,
      MessagePropertiesProvider propertiesProvider) {

    messagePropertiesReceived = propertiesProvider.get();
    send(routingKey, protoMessage);
  }

  @Override
  public void sendTo(
      Address replyTo, Message protoMessage, MessagePropertiesProvider propertiesProvider) {
    send(replyTo.getRoutingKey(), protoMessage);
  }

  @Override
  public Optional<Object> sendAndReceive(
      Message protoMessage, MessagePropertiesProvider propertiesProvider) {
    send(protoMessage, propertiesProvider);
    return Optional.empty();
  }

  public void reset() {
    routingKeyUsed = null;
    sentMessage = null;
    messagePropertiesReceived = null;
  }

  public <T> T getSentMessage(Class<T> messageType) {
    if (sentMessage == null)
      throw new IllegalStateException("No message sent!");

    return messageType.cast(sentMessage);
  }
}
