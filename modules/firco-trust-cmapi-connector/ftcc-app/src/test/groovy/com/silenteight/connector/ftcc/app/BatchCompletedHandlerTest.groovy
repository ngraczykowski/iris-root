package com.silenteight.connector.ftcc.app

import com.silenteight.connector.ftcc.callback.handler.BatchCompletedHandler
import com.silenteight.connector.ftcc.callback.handler.BatchCompletedService
import com.silenteight.connector.ftcc.callback.response.ResponseProcessor
import com.silenteight.connector.ftcc.common.testing.BaseSpecificationIT
import com.silenteight.proto.registration.api.v1.MessageBatchCompleted

import org.spockframework.spring.SpringBean
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import spock.lang.Unroll
import spock.util.concurrent.PollingConditions

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT

@SpringBootTest(webEnvironment = RANDOM_PORT, classes = [FtccApplication, BatchCompletedHandler])
@ActiveProfiles("dev")
class BatchCompletedHandlerTest extends BaseSpecificationIT {

  @Autowired
  private RabbitTemplate rabbitTemplate

  @Value('${ftcc.core-bridge.inbound.batch-completed.exchange}')
  String queueName

  @SpringBean
  BatchCompletedService batchCompletedService = Mock()

  @SpringBean
  ResponseProcessor responseProcessor = Mock()

  def setupSpec() {
    startRabbitmq()
    startPostgresql()
  }

  @Unroll
  def 'message should be received #routingKey'() {
    given:
    def conditions = new PollingConditions(timeout: 5, initialDelay: 0.2, factor: 1.25)

    MessageBatchCompleted batchCompleted = MessageBatchCompleted.newBuilder()
        .setBatchId('batch-id')
        .setAnalysisName('analysis-name')
        .build()

    String alertType
    responseProcessor.process(_, _) >> {MessageBatchCompleted messageBatchCompleted, String type ->
      alertType = type
    }

    when:
    println "Sending $batchCompleted to $queueName"
    rabbitTemplate.convertAndSend(queueName, routingKey, batchCompleted)

    then: 'message is received'
    noExceptionThrown()
    conditions.eventually {
      assert alertType == routingKey
    }

    where:
    routingKey << ['solving', 'simulation']
  }
}
