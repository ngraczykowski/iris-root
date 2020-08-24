package com.silenteight.sep.base.common.messaging;

import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.common.messaging.ErrorChannelIT.ErrorChannelConfiguration;
import com.silenteight.sep.base.testing.BaseIntegrationTest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;
import static org.awaitility.Awaitility.await;

@ContextConfiguration(classes = {
    ErrorChannelConnectionConfiguration.class,
    ErrorChannelConfiguration.class })
@TestPropertySource(properties = "serp.messaging.error-queue.enabled=true")
class ErrorChannelIT extends BaseIntegrationTest {

  private static final String INPUT_QUEUE = "inputQueue";
  public static final String EXCHANGE = "exchange";

  @Autowired
  private PublishSubscribeChannel errorChannel;

  @Autowired
  private RabbitTemplate template;

  @Autowired
  private AmqpAdmin amqpAdmin;

  @AfterEach
  public void cleanRabbitmqQueues() {
    amqpAdmin.deleteExchange(EXCHANGE);
    amqpAdmin.purgeQueue(INPUT_QUEUE);
    amqpAdmin.deleteQueue(INPUT_QUEUE);
  }

  @Test
  @Timeout(4)
  public void shouldAddToErrorQueue() {
    var exceptionCount = 3;

    IntStream
        .range(0, exceptionCount)
        .forEach(i -> template.convertAndSend(INPUT_QUEUE, "test: " + i));

    await().until(() -> errorChannel.getSendCount() >= exceptionCount);

    assertThat(exceptionCount).isEqualTo(errorChannel.getSendCount());
    assertThat(exceptionCount)
        .isEqualTo(amqpAdmin
            .getQueueProperties("error-queue")
            .get("QUEUE_MESSAGE_COUNT"));
  }

  @RequiredArgsConstructor
  @TestConfiguration
  static class ErrorChannelConfiguration {

    private final AmqpInboundFactory amqpInboundFactory;

    @Bean
    Declarables brokerTestConfiguration() {
      var inputQueue = QueueBuilder.durable(INPUT_QUEUE).build();
      DirectExchange exchange = ExchangeBuilder.directExchange("exchange").durable(true).build();
      return new Declarables(
          inputQueue,
          exchange,
          BindingBuilder.bind(inputQueue).to(exchange).withQueueName()
      );
    }

    @Bean
    IntegrationFlow inputIntegrationFlow() {
      return IntegrationFlows
          .from(amqpInboundFactory
                  .simpleAdapter()
                  .messageConverter(new SimpleMessageConverter())
                  .configureContainer(c -> c.addQueueNames(INPUT_QUEUE)))
          .<String>handle((p, h) -> {
            throw new IllegalStateException("Exception in handler: " + p);
          })
          .get();
    }
  }
}
