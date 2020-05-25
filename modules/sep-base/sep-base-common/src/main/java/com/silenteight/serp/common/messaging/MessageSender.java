package com.silenteight.serp.common.messaging;

import com.silenteight.serp.common.messaging.properties.MessagePropertiesProvider;

import com.google.protobuf.Message;
import org.springframework.amqp.core.Address;

import java.util.Optional;

public interface MessageSender {

  void send(Message protoMessage);

  void send(Message protoMessage, MessagePropertiesProvider propertiesProvider);

  void send(String routingKey, Message protoMessage);

  void send(String routingKey, Message protoMessage, MessagePropertiesProvider propertiesProvider);

  void sendTo(Address replyTo, Message protoMessage, MessagePropertiesProvider propertiesProvider);

  Optional<Object> sendAndReceive(Message message, MessagePropertiesProvider propertiesProvider);
}
