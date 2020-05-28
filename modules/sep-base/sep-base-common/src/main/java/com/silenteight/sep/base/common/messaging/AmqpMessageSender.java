package com.silenteight.sep.base.common.messaging;

import lombok.Builder;
import lombok.NonNull;

import com.silenteight.sep.base.common.messaging.properties.MessagePropertiesProvider;

import com.google.protobuf.Message;
import org.springframework.amqp.core.Address;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConverter;

import java.util.Collection;
import java.util.Optional;
import javax.annotation.Nullable;

import static java.util.Objects.nonNull;

@Builder
class AmqpMessageSender implements MessageSender {

  @NonNull
  private final String exchange;
  @NonNull
  private final AmqpTemplate template;
  @NonNull
  private final MessageConverter messageConverter;
  @NonNull
  private final Collection<SendMessageListener> listeners;

  private MessagePostProcessor additionalMessagePostProcessor;

  void setAdditionalMessagePostProcessor(
      @Nullable MessagePostProcessor additionalMessagePostProcessor) {
    this.additionalMessagePostProcessor = additionalMessagePostProcessor;
  }

  @Override
  public void send(Message protoMessage) {
    send(protoMessage, EmptyMessagePropertiesProvider.INSTANCE);
  }

  @Override
  public void send(Message protoMessage, MessagePropertiesProvider propertiesProvider) {
    send(null, protoMessage, propertiesProvider);
  }

  @Override
  public void send(@NonNull String routingKey, Message protoMessage) {
    send(routingKey, protoMessage, EmptyMessagePropertiesProvider.INSTANCE);
  }

  @Override
  public void send(
      @Nullable String routingKey,
      @NonNull Message protoMessage,
      @NonNull MessagePropertiesProvider propertiesProvider) {

    sendTo(new Address(exchange, routingKey != null ? routingKey : ""), protoMessage,
        propertiesProvider);
  }

  @Override
  public void sendTo(
      @NonNull Address address,
      @NonNull Message protoMessage,
      @NonNull MessagePropertiesProvider propertiesProvider) {

    org.springframework.amqp.core.Message amqpMessage = convertToAmqpMessage(
        protoMessage, propertiesProvider.get());

    template.send(address.getExchangeName(), address.getRoutingKey(), amqpMessage);
    onMessageSent(amqpMessage);
  }

  @Override
  public Optional<Object> sendAndReceive(
      @NonNull Message protoMessage,
      @NonNull MessagePropertiesProvider propertiesProvider) {
    org.springframework.amqp.core.Message amqpMessage = convertToAmqpMessage(
        protoMessage, propertiesProvider.get());
    org.springframework.amqp.core.Message responseMessage = template.sendAndReceive(amqpMessage);

    if (nonNull(responseMessage))
      return Optional.of(messageConverter.fromMessage(responseMessage));

    return Optional.empty();
  }

  private org.springframework.amqp.core.Message convertToAmqpMessage(
      Message protoMessage,
      MessageProperties messageProperties) {

    org.springframework.amqp.core.Message amqpMessage = messageConverter.toMessage(
        protoMessage, messageProperties);

    if (additionalMessagePostProcessor != null)
      amqpMessage = additionalMessagePostProcessor.postProcessMessage(amqpMessage);

    return amqpMessage;
  }

  private void onMessageSent(org.springframework.amqp.core.Message messageBody) {
    listeners.forEach(l -> l.onSent(messageBody));
  }

  enum EmptyMessagePropertiesProvider implements MessagePropertiesProvider {
    INSTANCE;

    @Override
    public MessageProperties get() {
      return new MessageProperties();
    }
  }
}
