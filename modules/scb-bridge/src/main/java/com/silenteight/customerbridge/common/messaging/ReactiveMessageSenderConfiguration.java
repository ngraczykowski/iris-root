package com.silenteight.customerbridge.common.messaging;

import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.common.messaging.ProtoMessageConverter;
import com.silenteight.sep.base.common.messaging.postprocessing.SepMessagePostProcessors;

import com.rabbitmq.client.Connection;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;
import reactor.rabbitmq.*;
import reactor.util.retry.Retry;

import java.time.Duration;

@RequiredArgsConstructor
@Configuration
class ReactiveMessageSenderConfiguration {

  private final ConnectionFactory connectionFactory;
  private final ProtoMessageConverter protoMessageConverter;
  private final Exchange alertUnprocessed;
  private final SepMessagePostProcessors postProcessors;

  @Bean
  ReactiveMessageSender reactiveMessageSender() {
    var alertSender = new ReactiveMessageSender(rabbitSender(), protoMessageConverter);

    alertSender.setPostProcessor(postProcessors.getSendPostProcessor());

    alertSender.setDefaultExchange(alertUnprocessed.getName());

    return alertSender;
  }

  private Sender rabbitSender() {
    var senderOptions = new SenderOptions()
        .connectionMono(senderConnectionMono())
        .connectionMonoConfigurator(this::configureConnectionMono)
        .channelPool(senderChannelPool());

    return RabbitFlux.createSender(senderOptions);
  }

  private ChannelPool senderChannelPool() {
    return ChannelPoolFactory.createChannelPool(senderConnectionMono());
  }

  private Mono<Connection> senderConnectionMono() {
    return getConnectionMono();
  }

  private Mono<Connection> getConnectionMono() {
    return Mono.fromCallable(() -> connectionFactory.createConnection().getDelegate()).cache();
  }

  private Mono<? extends Connection> configureConnectionMono(
      Mono<? extends Connection> connectionMono) {

    return connectionMono.retryWhen(Retry.backoff(3, Duration.ofSeconds(5)));
  }
}
