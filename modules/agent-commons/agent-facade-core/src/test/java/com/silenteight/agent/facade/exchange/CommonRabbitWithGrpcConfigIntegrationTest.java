package com.silenteight.agent.facade.exchange;

import com.silenteight.agent.facade.exchange.CommonRabbitWithGrpcConfigIntegrationTest.RabbitWithGrpcInitializer;
import com.silenteight.agents.v1.api.exchange.AgentExchangeRequest;
import com.silenteight.agents.v1.api.exchange.AgentExchangeResponse;

import com.google.protobuf.Any;
import com.google.protobuf.InvalidProtocolBufferException;
import org.junit.jupiter.api.Test;
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

import static com.silenteight.agent.facade.exchange.RabbitBrokerTestConfiguration.TEST_FACADE_OUT_QUEUE;
import static com.silenteight.agent.facade.exchange.RabbitIntegrationTestFixtures.RABBIT_DEFAULT_PORT;
import static com.silenteight.agent.facade.exchange.RabbitIntegrationTestFixtures.RABBIT_DOCKER_IMAGE;
import static com.silenteight.agent.facade.exchange.RabbitIntegrationTestFixtures.TIMEOUT;

/**
 * Rabbit Integration tests with RemoteDataSourceClient - needs to be run only with
 * "rabbitmq-declare" profile
 */
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.NONE,
    properties = "grpc.server.port=0")
@ContextConfiguration(initializers = RabbitWithGrpcInitializer.class)
@DirtiesContext
@Testcontainers
public abstract class CommonRabbitWithGrpcConfigIntegrationTest {

  private static final String DEADLINE_ERROR_MESSAGE_PATTERN =
      "DEADLINE_EXCEEDED:.*deadline exceeded.*";
  private static final String DATA_SOURCE_ERROR = "DATA_SOURCE_ERROR";
  private static final String TEST_FEATURE = "test-feature";
  private static final String TEST_MATCH = "test-match";
  private static final RabbitMQContainer RABBIT;

  @Autowired
  private RabbitTemplate rabbitTemplate;

  protected abstract String getExchangeName();

  protected abstract String getRoutingKey();

  static {
    RABBIT = new RabbitMQContainer(RABBIT_DOCKER_IMAGE);
    RABBIT.start();
  }

  /**
   * Grpc deadline is set to 1s in every agent tests properties to make test quicker, rabbit has
   * timeout set to 5s on message receiving. In this condition even without it's possible to receive
   * error message and verify that it's caused by deadline exceeding
   */
  @Test
  void shouldExceedGrpcDeadlineAndReturnError() throws InvalidProtocolBufferException {
    var agentExchangeRequest = AgentExchangeRequest.newBuilder()
        .addAllFeatures(List.of("test-feature"))
        .addAllMatches(List.of("test-match"))
        .build();

    rabbitTemplate.convertAndSend(getExchangeName(), getRoutingKey(), agentExchangeRequest);

    var agentExchangeResponse = receiveMessage();
    var agentExchangeResponseAssert = new AgentExchangeResponseAssert(agentExchangeResponse);
    agentExchangeResponseAssert
        .hasSingleOutput()
        .withMatch(TEST_MATCH)
        .hasSingleFeature()
        .withFeature(TEST_FEATURE)
        .hasSingleFeatureSolution()
        .withSolution(DATA_SOURCE_ERROR)
        .withErrorMessageMatchesPhrase(DEADLINE_ERROR_MESSAGE_PATTERN);
  }

  private AgentExchangeResponse receiveMessage() throws InvalidProtocolBufferException {
    var message = rabbitTemplate.receiveAndConvert(TEST_FACADE_OUT_QUEUE, TIMEOUT);
    if (message != null) {
      return ((Any) message).unpack(AgentExchangeResponse.class);
    }
    return AgentExchangeResponse.newBuilder().build();
  }

  static class RabbitWithGrpcInitializer implements
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
