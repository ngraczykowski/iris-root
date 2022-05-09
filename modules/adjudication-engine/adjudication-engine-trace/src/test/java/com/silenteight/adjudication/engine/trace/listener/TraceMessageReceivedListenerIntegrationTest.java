package com.silenteight.adjudication.engine.trace.listener;

import lombok.AllArgsConstructor;
import lombok.Data;

import com.silenteight.adjudication.engine.trace.AdjudicationEngineTraceModule;
import com.silenteight.sep.base.common.support.jackson.JsonConversionHelper;
import com.silenteight.sep.base.testing.containers.PostgresContainer.PostgresTestInitializer;
import com.silenteight.sep.base.testing.containers.RabbitContainer.RabbitTestInitializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.codec.digest.DigestUtils;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.test.RabbitListenerTest;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.jdbc.AutoConfigureDataJdbc;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.Serializable;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.stream.IntStream;

import static com.silenteight.adjudication.engine.trace.listener.TraceMessageReceivedListener.TRACKING_EXCHANGE_NAME;
import static com.silenteight.adjudication.engine.trace.listener.TraceMessageReceivedListener.TRACKING_ROUTING_KEY;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@RabbitListenerTest(spy = false, capture = true)
@ComponentScan(basePackageClasses = AdjudicationEngineTraceModule.class)
@ContextConfiguration(initializers = {
    RabbitTestInitializer.class,
    PostgresTestInitializer.class })
@ImportAutoConfiguration({
    RabbitAutoConfiguration.class
})
@AutoConfigureDataJdbc
@TestPropertySource(properties = {
    "spring.liquibase.change-log=classpath:db/changelog/db.changelog-test.xml",
    "ae.solving.journal.enabled=true",
    "spring.rabbitmq.listener.simple.concurrency=20"
})
class TraceMessageReceivedListenerIntegrationTest {

  @Autowired
  private RabbitTemplate rabbitTemplate;
  @Autowired
  private JdbcTemplate jdbcTemplate;

  private static final ObjectMapper OBJECT_MAPPER = JsonConversionHelper.INSTANCE
      .objectMapper().registerModule(new JavaTimeModule());

  private static final Jackson2JsonMessageConverter MESSAGE_CONVERTER =
      new Jackson2JsonMessageConverter(OBJECT_MAPPER);

  @DisplayName("Sending messages should be stored in DB")
  @ParameterizedTest
  @ValueSource(ints = { 1, 20 })
  void onReceivedTrace(int amount) {
    // given
    final var message = new JournalMessage(1L, UUID.randomUUID(), "event.type.name",
        "{"
            + "\"name\": \"alert/name\","
            + " \"alertSolvingId\": 111"
            + "}", OffsetDateTime.now(Clock.systemUTC()));

    // when
    IntStream.range(0, amount).parallel().forEach(value -> send(message));

    // then
    var query =
        jdbcTemplate.query(
            "select recommendation_hash, payload from ae_event_journal", mapper());
    assertThat(query).isNotNull().hasSize(amount)
        .extracting("payload", "recommendationHash")
        .contains(tuple(message.getPayload(), null));
  }

  @Test
  @DisplayName("Sending messages with payload contains recommendation, should calculate hash")
  void shouldCalculateHash() {
    // given
    final var recommendation = "recommendation: alert 'brrrt'";
    final var message = new JournalMessage(1L, UUID.randomUUID(), "event.type.name",
        "{"
            + "\"name\": \"alert/name\","
            + " \"alertSolvingId\": 111,"
            + " \"recommendation\": \"" + recommendation + "\""
            + "}", OffsetDateTime.now(Clock.systemUTC()));

    // when
    send(message);

    // then
    var query =
        jdbcTemplate.query(
            "select recommendation_hash, payload from ae_event_journal", mapper());
    assertThat(query).isNotNull().hasSize(1)
        .extracting("payload", "recommendationHash")
        .contains(tuple(message.getPayload(), DigestUtils.sha512Hex(recommendation)));
  }

  @DisplayName("Passing wrong message type should not process")
  @Test
  void sendWrongObject() {
    // when
    IntStream
        .range(0, 1)
        .forEach(value -> {
          try {
            send(OBJECT_MAPPER.readTree(
                "{\"id\": \"" + UUID.randomUUID()
                    + "\",\"eventType\":\"feature.find.all\", "
                    + "\"payload\":\"{}\", \"alertSolvingId\": 1}"));
          } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
          }
        });

    // then
    var query =
        jdbcTemplate.query(
            "select recommendation_hash, payload from ae_event_journal", mapper());
    assertThat(query).isNotNull().isEmpty();
  }

  private void send(Object o) {
    this.rabbitTemplate.convertSendAndReceive(TRACKING_EXCHANGE_NAME, TRACKING_ROUTING_KEY, o);
  }

  @AfterEach
  void cleanUp() {
    jdbcTemplate.execute("truncate ae_event_journal");
  }

  @NotNull
  private static RowMapper<TraceEntity> mapper() {
    return (rs, rowNum) -> new TraceEntity(rs.getString(1), rs.getString(2));
  }

  @AllArgsConstructor
  static class TraceEntity implements Serializable {

    private static final long serialVersionUID = 8381875689481139763L;
    String recommendationHash;
    String payload;
  }

  @Data
  @AllArgsConstructor
  static class JournalMessage implements Serializable {

    private static final long serialVersionUID = 9018650900023544179L;
    Long alertSolvingId;
    UUID id;
    String eventType;
    String payload;
    OffsetDateTime occurredOn;
  }
}
