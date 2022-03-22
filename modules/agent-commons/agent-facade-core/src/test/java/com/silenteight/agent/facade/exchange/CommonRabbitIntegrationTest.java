package com.silenteight.agent.facade.exchange;

import com.silenteight.agent.facade.exchange.CommonRabbitIntegrationTest.Initializer;
import com.silenteight.agents.v1.api.exchange.AgentExchangeRequest;
import com.silenteight.agents.v1.api.exchange.AgentExchangeResponse;

import com.google.protobuf.Any;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.Timestamp;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;

import static com.silenteight.agent.facade.exchange.RabbitBrokerTestConfiguration.TEST_FACADE_OUT_QUEUE;
import static com.silenteight.agent.facade.exchange.RabbitIntegrationTestFixtures.RABBIT_DEFAULT_PORT;
import static com.silenteight.agent.facade.exchange.RabbitIntegrationTestFixtures.RABBIT_DOCKER_IMAGE;
import static com.silenteight.agent.facade.exchange.RabbitIntegrationTestFixtures.TIMEOUT;
import static java.time.Instant.now;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

// Note: test methods will be executed by test classes extending this one
@SuppressWarnings("unused")

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.NONE,
    properties = "grpc.server.port=0")
@ContextConfiguration(initializers = Initializer.class)
@DirtiesContext
@Testcontainers
@TestInstance(Lifecycle.PER_CLASS)
public abstract class CommonRabbitIntegrationTest {

  private static final RabbitMQContainer RABBIT;

  @Autowired
  private RabbitTemplate rabbitTemplate;

  static {
    RABBIT = new RabbitMQContainer(RABBIT_DOCKER_IMAGE);
    RABBIT.start();
  }

  protected abstract String getFeatureName();

  protected abstract String getExchangeName();

  protected abstract String getRoutingKey();

  protected abstract String getDeadLetterQueueName();

  protected abstract Map<String, String> getMatchesWithExpectedResults();

  protected void sendMessage(Object body) {
    rabbitTemplate.convertAndSend(getExchangeName(), getRoutingKey(), body);
  }

  protected AgentExchangeResponse receiveMessage() {
    return receiveMessage(TEST_FACADE_OUT_QUEUE, AgentExchangeResponse.class);
  }

  protected <T extends Message> T receiveMessageFromDeadLetterQueue(Class<T> clazz) {
    return receiveMessage(getDeadLetterQueueName(), clazz);
  }

  private <T extends Message> T receiveMessage(String queueName, Class<T> clazz) {
    var message = rabbitTemplate.receiveAndConvert(queueName, TIMEOUT);
    if (message == null) {
      fail("There is no message in " + queueName);
    }
    return parseMessage((Any) message, clazz);
  }

  private <T extends Message> T parseMessage(Any any, Class<T> clazz) {
    try {
      return any.unpack(clazz);
    } catch (InvalidProtocolBufferException e) {
      return fail("Can not parse message to " + clazz);
    }
  }

  private Object[] provideMatches() {
    return getMatchesWithExpectedResults().keySet().toArray();
  }

  @Test
  void shouldReceiveAndProcessRequestWithNotSupportedFeature() {
    //given
    AgentExchangeRequest agentExchangeRequest = AgentExchangeRequest.newBuilder()
        .addAllFeatures(List.of("not-supported-agent-feature"))
        .addAllMatches(List.of("any-match"))
        .build();

    sendMessage(agentExchangeRequest);

    //when
    var agentExchangeResponse = receiveMessage();

    //then
    assertThat(agentExchangeResponse)
        .isNotNull()
        .isEqualTo(AgentExchangeResponse.getDefaultInstance());
  }

  @Test
  void shouldReceiveAndProcessEmptyRequest() {
    //given
    AgentExchangeRequest agentExchangeRequest = AgentExchangeRequest.getDefaultInstance();
    sendMessage(agentExchangeRequest);

    //when
    var agentExchangeResponse = receiveMessage();

    //then
    assertThat(agentExchangeResponse)
        .isNotNull()
        .isEqualTo(AgentExchangeResponse.getDefaultInstance());
  }

  @Test
  void shouldReceiveInvalidMessageAndSendToDeadLetterQueue() {
    //given
    var invalidMessage = AgentExchangeResponse.getDefaultInstance();
    sendMessage(invalidMessage);

    //when
    var invalidMessageResponse = receiveMessageFromDeadLetterQueue(invalidMessage.getClass());

    //then
    assertThat(invalidMessage).isEqualTo(invalidMessageResponse);
  }

  @Test
  void shouldReceiveAndProcessMessageWithMissingMatchId() {
    //given
    var matchIds =
        List.of("not-supported-matchId1", "not-supported-matchId2", "not-supported-matchId3");

    AgentExchangeRequest agentExchangeRequest = AgentExchangeRequest.newBuilder()
        .addAllFeatures(List.of(getFeatureName()))
        .addAllMatches(matchIds)
        .build();

    sendMessage(agentExchangeRequest);

    //when
    var agentExchangeResponse = receiveMessage();

    //then
    var agentExchangeResponseAssert = new AgentExchangeResponseAssert(agentExchangeResponse);
    agentExchangeResponseAssert.hasOutputSize(3);

    for (int i = 0; i < matchIds.size(); i++) {
      agentExchangeResponseAssert.getOutput(i)
          .withMatch("not-supported-matchId" + (i + 1))
          .hasSingleFeature()
          .withFeature(getFeatureName())
          .hasSingleFeatureSolution()
          .withSolution("DATA_SOURCE_ERROR")
          .withErrorMessage("Simulating Data Source error");
    }
  }

  @Test
  void shouldReceiveAndProcessMessageWithDeadlineTimeExceeded() {
    //given
    var deadlineTime = now().getEpochSecond() - 1000;

    AgentExchangeRequest agentExchangeRequest = AgentExchangeRequest.newBuilder()
        .addAllFeatures(List.of(getFeatureName()))
        .addAllMatches(List.of("match1"))
        .setDeadlineTime(Timestamp
            .newBuilder()
            .setSeconds(deadlineTime)
            .build())
        .build();

    sendMessage(agentExchangeRequest);

    //when
    var agentExchangeResponse = receiveMessage();

    //then
    var agentExchangeResponseAssert = new AgentExchangeResponseAssert(agentExchangeResponse);
    agentExchangeResponseAssert
        .hasSingleOutput()
        .withMatch("match1")
        .hasSingleFeature()
        .withFeature(getFeatureName())
        .hasSingleFeatureSolution()
        .withSolution("DEADLINE_EXCEEDED")
        .withErrorMessage("Agent Request deadline time (" + deadlineTime + ")  exceeded");
  }

  @ParameterizedTest(name = "{0} should be received and processed with expected result")
  @MethodSource("provideMatches")
  protected void shouldReceiveAndProcessMessageWithMatch(String match) {
    //given
    var matchesWithExpectedResults = getMatchesWithExpectedResults();
    AgentExchangeRequest agentExchangeRequest = AgentExchangeRequest.newBuilder()
        .addAllFeatures(List.of(getFeatureName()))
        .addAllMatches(List.of(match))
        .build();

    sendMessage(agentExchangeRequest);

    //when
    var agentExchangeResponse = receiveMessage();

    //then
    var agentExchangeResponseAssert = new AgentExchangeResponseAssert(agentExchangeResponse);
    agentExchangeResponseAssert
        .hasSingleOutput()
        .withMatch(match)
        .hasSingleFeature()
        .withFeature(getFeatureName())
        .hasSingleFeatureSolution()
        .withNoErrorMessage()
        .withSolution(matchesWithExpectedResults.get(match));
  }

  @Test
  protected void shouldReceiveAndProcessMessageWithMultipleMatches() {
    //given
    var matchesWithExpectedResults = getMatchesWithExpectedResults();
    AgentExchangeRequest agentExchangeRequest = AgentExchangeRequest.newBuilder()
        .addAllFeatures(List.of(getFeatureName()))
        .addAllMatches(matchesWithExpectedResults.keySet())
        .build();

    sendMessage(agentExchangeRequest);

    //when
    var agentExchangeResponse = receiveMessage();

    //then
    var agentExchangeResponseAssert = new AgentExchangeResponseAssert(agentExchangeResponse);
    agentExchangeResponseAssert.hasOutputSize(matchesWithExpectedResults.size());
    matchesWithExpectedResults.entrySet()
        .forEach(assertExchangeResponseForMatch(agentExchangeResponseAssert));
  }

  private Consumer<Entry<String, String>> assertExchangeResponseForMatch(
      AgentExchangeResponseAssert agentExchangeResponseAssert) {
    return matchWithExpectedResult -> agentExchangeResponseAssert
        .getOutputForMatch(matchWithExpectedResult.getKey())
        .hasSingleFeature()
        .withFeature(getFeatureName())
        .hasSingleFeatureSolution()
        .withNoErrorMessage()
        .withSolution(matchWithExpectedResult.getValue());
  }

  static class Initializer implements
      ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
      var values = TestPropertyValues.of(
          "spring.rabbitmq.host=" + RABBIT.getContainerIpAddress(),
          "spring.rabbitmq.port=" + RABBIT.getMappedPort(RABBIT_DEFAULT_PORT)
      );
      values.applyTo(configurableApplicationContext);
    }
  }
}
