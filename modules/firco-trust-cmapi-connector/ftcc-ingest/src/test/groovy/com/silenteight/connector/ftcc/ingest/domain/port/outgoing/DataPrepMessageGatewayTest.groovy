package com.silenteight.connector.ftcc.ingest.domain.port.outgoing

import com.silenteight.connector.ftcc.common.testing.BaseRabbitMQSpecificationIT
import com.silenteight.connector.ftcc.ingest.adapter.outgoing.amqp.DataPrepAmqpIntegrationFlowConfiguration
import com.silenteight.connector.ftcc.ingest.adapter.outgoing.amqp.DataPrepProperties
import com.silenteight.sep.base.common.messaging.IntegrationConfiguration
import com.silenteight.sep.base.common.messaging.MessagingConfiguration

import org.springframework.amqp.core.*
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration
import org.springframework.boot.autoconfigure.integration.IntegrationAutoConfiguration
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

import javax.validation.Valid

import static com.silenteight.proto.fab.api.v1.AlertMessageStored.State.*
import static com.silenteight.rabbitcommonschema.definitions.RabbitConstants.CONNECTOR_COMMAND_EXCHANGE

@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = [
    DataPrepAmqpIntegrationFlowConfiguration,
    IntegrationConfiguration,
    MessagingConfiguration,
    IntegrationAutoConfiguration,
    RabbitAutoConfiguration,
    DataPrepMessageGatewayTestConfig])
class DataPrepMessageGatewayTest extends BaseRabbitMQSpecificationIT {

  @Autowired
  private DataPrepMessageGateway underTest

  @Autowired
  private RabbitTemplate rabbitTemplate

  def "When sending 2 messages priority is respected"() {
    given:
    def solvingMessage = AlertMessage.builder()
        .batchName("batches/805fa4f0-b64a-11ec-a3db-b799ff431660")
        .messageName("messages/5632d79c-b64a-11ec-94f8-8f2692f2ddc0")
        .state(NEW)
        .priority(2)
        .build()

    def learningMessage = AlertMessage.builder()
        .batchName("batches/805fa4f0-b64a-11ec-a3db-b799ff431661")
        .messageName("messages/5632d79c-b64a-11ec-94f8-8f2692f2ddc1")
        .state(STATE_UNSPECIFIED)
        .priority(1)
        .build()

    when: 'sending learning then solving message to queue'
    underTest.send(learningMessage)
    underTest.send(solvingMessage)

    then: 'solving message is received before learning message'
    noExceptionThrown()
    Message solvingMessageReceived = rabbitTemplate
        .receive(DataPrepMessageGatewayTestConfig.QUEUE_NAME_PROPERTY)
    assert getPriority(solvingMessageReceived) == solvingMessage.getPriority()
    Message learningMessageReceived = rabbitTemplate
        .receive(DataPrepMessageGatewayTestConfig.QUEUE_NAME_PROPERTY)
    assert getPriority(learningMessageReceived) == learningMessage.getPriority()
  }

  private int getPriority(Message message) {
    return message.messageProperties.getHeader('priority')
  }
}

@EnableConfigurationProperties(DataPrepProperties)
@TestConfiguration
class DataPrepMessageGatewayTestConfig {

  static final QUEUE_NAME_PROPERTY = "alert.messages"

  @Bean
  Queue alertMessagesQueue() {
    return QueueBuilder.durable(QUEUE_NAME_PROPERTY).maxPriority(10).build()
  }

  @Bean
  DirectExchange alertMessagesExchange() {
    return new DirectExchange(CONNECTOR_COMMAND_EXCHANGE)
  }

  @Bean
  Binding feedingBinding(
      Queue alertMessagesQueue, DirectExchange alertMessagesExchange,
      @Valid DataPrepProperties dataPrepProperties) {
    return BindingBuilder
        .bind(alertMessagesQueue)
        .to(alertMessagesExchange)
        .with(dataPrepProperties.getOutbound().getRoutingKey())
  }
}
