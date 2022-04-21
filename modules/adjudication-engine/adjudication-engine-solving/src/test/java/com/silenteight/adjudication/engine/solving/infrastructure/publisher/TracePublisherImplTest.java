package com.silenteight.adjudication.engine.solving.infrastructure.publisher;

import com.silenteight.adjudication.engine.solving.application.publisher.TracePublisher;
import com.silenteight.adjudication.engine.solving.domain.AlertSolving;
import com.silenteight.adjudication.engine.solving.domain.TraceEvent;
import com.silenteight.adjudication.engine.solving.domain.event.FeatureMatchesUpdated;
import com.silenteight.adjudication.engine.solving.domain.event.MatchFeatureValuesUpdated;
import com.silenteight.adjudication.engine.solving.domain.event.MatchesUpdated;
import com.silenteight.sep.base.testing.containers.RabbitContainer.RabbitTestInitializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.test.RabbitListenerTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

@RabbitListenerTest(spy = false, capture = true)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(initializers = { RabbitTestInitializer.class })
@ImportAutoConfiguration({ RabbitAutoConfiguration.class, })
class TracePublisherImplTest {

  public static final String QUEUEEEEE_NAME = "queueeeee";
  private static final ObjectMapper OBJECT_MAPPER =
      new ObjectMapper()
          .registerModule(new JavaTimeModule());

  private TracePublisher tracePublisher;

  @Autowired
  private RabbitTemplate rabbitTemplate;
  @Autowired
  private RabbitAdmin rabbitAdmin;

  @DisplayName("Send message, deserialize to proper type")
  @ParameterizedTest
  @MethodSource("testData")
  void testSendingMessage(TraceEvent event, Class<? extends TraceEvent> clazz) throws IOException {
    tracePublisher.publish(event);
    var obj = obtainMessage();
    assertThat(obj)
        .isNotNull()
        .isInstanceOf(clazz)
        .hasFieldOrPropertyWithValue("occurredOn", event.occurredOn())
        .hasFieldOrPropertyWithValue("id", event.id());
  }

  @BeforeEach
  void setUp() {
    var exchange = ExchangeBuilder.topicExchange("ae.event.internal.tracing").build();
    var queueeeee = QueueBuilder.durable(QUEUEEEEE_NAME).build();
    rabbitAdmin.declareExchange(exchange);
    rabbitAdmin.declareQueue(queueeeee);
    rabbitAdmin.declareBinding(BindingBuilder.bind(queueeeee).to(exchange).with("").noargs());

    tracePublisher = new TracePublisherImpl(rabbitTemplate);
  }

  private static Stream<Arguments> testData() {
    return Stream.of(
        Arguments.of(
            new FeatureMatchesUpdated(new AlertSolving(1L, "policy", "strategy", 1)),
            FeatureMatchesUpdated.class),
        Arguments.of(
            new MatchesUpdated(new AlertSolving(2L, "policy", "strategy", 1)),
            MatchesUpdated.class),
        Arguments.of(
            new MatchFeatureValuesUpdated(new AlertSolving(20L, "policy", "strategy", 1)),
            MatchFeatureValuesUpdated.class)
    );
  }

  private TraceEvent obtainMessage() throws IOException {
    final var domainEvent = this.rabbitTemplate.receiveAndConvert(QUEUEEEEE_NAME, 4000);
    return OBJECT_MAPPER.readValue(((byte[]) domainEvent), TraceEvent.class);
  }
}
