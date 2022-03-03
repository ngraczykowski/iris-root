package com.silenteight.customerbridge.common.messaging;

import lombok.RequiredArgsConstructor;
import lombok.Setter;

import com.silenteight.sep.base.common.messaging.properties.MessagePriorityProvider;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.support.DefaultMessagePropertiesConverter;
import org.springframework.amqp.rabbit.support.MessagePropertiesConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.rabbitmq.OutboundMessage;
import reactor.rabbitmq.Sender;

import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
public class ReactiveMessageSender {

  private final Sender sender;
  private final MessageConverter messageConverter;

  @Setter
  private MessagePostProcessor postProcessor;
  @Setter
  private String defaultExchange = "";
  @Setter
  private String defaultRoutingKey = "";
  @Setter
  private MessagePropertiesConverter propertiesConverter = new DefaultMessagePropertiesConverter();

  public Mono<Void> send(Flux<OutboundProtoMessage> protoMessages) {
    return sender.send(protoMessages.map(this::makeOutboundMessage));
  }

  private OutboundMessage makeOutboundMessage(OutboundProtoMessage protoMessage) {
    var exchange = protoMessage.getExchange().orElse(defaultExchange);
    var routingKey = protoMessage.getRoutingKey().orElse(defaultRoutingKey);
    var amqpMessage = makeAmqpMessage(protoMessage);
    var basicProperties =
        propertiesConverter.fromMessageProperties(amqpMessage.getMessageProperties(), "UTF-8");

    return new OutboundMessage(exchange, routingKey, basicProperties, amqpMessage.getBody());
  }

  private Message makeAmqpMessage(OutboundProtoMessage protoMessage) {
    var messagePriorityProvider = new MessagePriorityProvider(protoMessage.getPriority());
    var amqpMessage =
        messageConverter.toMessage(protoMessage.getMessage(), messagePriorityProvider.get());

    return ofNullable(postProcessor)
        .map(postProc -> postProc.postProcessMessage(amqpMessage))
        .orElse(amqpMessage);
  }
}

