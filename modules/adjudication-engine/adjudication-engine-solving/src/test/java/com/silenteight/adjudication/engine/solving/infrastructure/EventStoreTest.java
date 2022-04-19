package com.silenteight.adjudication.engine.solving.infrastructure;

import com.silenteight.adjudication.engine.solving.domain.AlertSolving;
import com.silenteight.adjudication.engine.solving.domain.DomainEvent;
import com.silenteight.adjudication.engine.solving.domain.event.FeatureMatchesUpdated;
import com.silenteight.sep.base.testing.containers.RabbitContainer.RabbitTestInitializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hazelcast.config.Config;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.instance.impl.HazelcastInstanceFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.test.RabbitListenerTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;

@RabbitListenerTest(spy = false, capture = true)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(initializers = { RabbitTestInitializer.class })
@ImportAutoConfiguration({ RabbitAutoConfiguration.class, })
public class EventStoreTest {

  private static final String TEST_QUEUE = "ae.event.store.queue";
  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
      .registerModule(new JavaTimeModule());
  private EventStore eventStore;

  @Autowired
  private RabbitTemplate rabbitTemplate;

  @Autowired
  private RabbitAdmin rabbitAdmin;

  @Test
  @DisplayName("Check message can be send without errors")
  public void checkMessageCanBeSendWithoutErrors() throws IOException {
    var event = new FeatureMatchesUpdated(new AlertSolving(1L));
    this.eventStore.publish(List.of(event));

    final DomainEvent domainEvent1 = obtainMessage();
    Assertions.assertNotNull(domainEvent1);
    Assertions.assertEquals(1L, domainEvent1.alertSolvingId());
  }

  @BeforeEach
  public void setUp() {
    final String exchangeName = "some.exchange";
    final String routingKey = "event.store.routing.key";
    final EventStoreConfigurationProperties eventStoreConfigurationProperties =
        new EventStoreConfigurationProperties(exchangeName, routingKey);
    final Queue queue = new Queue(TEST_QUEUE, true, true, true);
    this.rabbitAdmin.declareQueue(queue);

    final Exchange exchange = ExchangeBuilder
        .topicExchange(exchangeName)
        .build();
    this.rabbitAdmin.declareExchange(exchange);

    final var binding = BindingBuilder.bind(queue)
        .to(exchange)
        .with(routingKey)
        .noargs();

    this.rabbitAdmin.declareBinding(binding);
    final HazelcastInstance hazelcastInstance =
        HazelcastInstanceFactory.getOrCreateHazelcastInstance(
            new Config().setInstanceName("alert.solving"));
    this.eventStore =
        new EventStore(
            this.rabbitTemplate, eventStoreConfigurationProperties, hazelcastInstance,
            Executors.newSingleThreadScheduledExecutor()
        );
  }

  private DomainEvent obtainMessage() throws IOException {
    final Object domainEvent = this.rabbitTemplate.receiveAndConvert(TEST_QUEUE, 5000);

    return OBJECT_MAPPER.readValue(((byte[]) domainEvent), DomainEvent.class);
  }

}
