package com.silenteight.sep.base.common.messaging;

import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.common.messaging.ErrorChannelDisabledIT.QueueConfiguration;
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
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.*;
import static org.awaitility.Awaitility.await;

@ContextConfiguration(classes = {
    ErrorChannelConnectionConfiguration.class,
    QueueConfiguration.class })
@TestPropertySource(properties = "serp.messaging.error-queue.enabled=false")
class ErrorChannelDisabledIT extends BaseIntegrationTest {

  private static final String INPUT_QUEUE = "inputQueue";
  public static final String EXCHANGE = "exchange";

  @Autowired
  private RabbitTemplate template;

  @Autowired
  private AtomicInteger receivedMessageCounter;

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
  public void shouldRetryFailedMessage() {
    template.convertAndSend(INPUT_QUEUE, "test");

    await().until(() -> receivedMessageCounter.get() >= 2);

    assertThat(receivedMessageCounter.get()).isGreaterThanOrEqualTo(2);
  }

  @RequiredArgsConstructor
  @TestConfiguration
  static class QueueConfiguration {

    private final AmqpInboundFactory amqpInboundFactory;

    @Bean
    Declarables queueToExchangeBinding() {
      var inputQueue = QueueBuilder.durable(INPUT_QUEUE).build();
      DirectExchange exchange = ExchangeBuilder.directExchange(EXCHANGE).durable(true).build();
      return new Declarables(
          inputQueue,
          exchange,
          BindingBuilder.bind(inputQueue).to(exchange).withQueueName()
      );
    }

    @Bean
    IntegrationFlow receivedCounterIntegrationFlow(AtomicInteger receivedMessageCounter) {
      return IntegrationFlows
          .from(amqpInboundFactory
              .simpleAdapter()
              .messageConverter(new SimpleMessageConverter())
              .configureContainer(c -> c.addQueueNames(INPUT_QUEUE)))
          .<String>handle((p, h) -> {
            receivedMessageCounter.incrementAndGet();
            throw new IllegalStateException("Exception in handler: " + p);
          })
          .get();
    }

    @Bean
    AtomicInteger receivedMessageCounter() {
      return new AtomicInteger();
    }
  }
}
