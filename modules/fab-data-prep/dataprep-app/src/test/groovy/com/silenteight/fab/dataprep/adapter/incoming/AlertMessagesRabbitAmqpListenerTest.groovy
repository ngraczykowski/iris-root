package com.silenteight.fab.dataprep.adapter.incoming

import com.silenteight.fab.dataprep.BaseSpecificationIT
import com.silenteight.fab.dataprep.domain.DataPrepFacade
import com.silenteight.proto.fab.api.v1.AlertMessageStored
import com.silenteight.proto.fab.api.v1.AlertMessageStored.State

import org.spockframework.spring.SpringBean
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import spock.util.concurrent.PollingConditions

import static com.silenteight.fab.dataprep.adapter.incoming.AlertMessagesRabbitAmqpListener.QUEUE_NAME_PROPERTY
import static com.silenteight.fab.dataprep.domain.Fixtures.BATCH_NAME
import static com.silenteight.fab.dataprep.domain.Fixtures.MESSAGE_NAME

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@Import(IngestFlowRabbitMqTestConfig.class)
@ActiveProfiles("dev")
class AlertMessagesRabbitAmqpListenerTest extends BaseSpecificationIT {

  @Autowired
  private RabbitTemplate rabbitTemplate

  @SpringBean
  DataPrepFacade dataPrepFacade = Mock()

  @Value(QUEUE_NAME_PROPERTY)
  String queueName

  def solvingMessage = AlertMessageStored.newBuilder()
      .setBatchName(BATCH_NAME)
      .setMessageName(MESSAGE_NAME)
      .setState(State.NEW)
      .build()

  def learningMessage = AlertMessageStored.newBuilder()
      .setBatchName(BATCH_NAME)
      .setMessageName(MESSAGE_NAME)
      .setState(State.SOLVED_TRUE_POSITIVE)
      .build()

  def setupSpec() {
    startRabbitmq()
    startPostgresql()   //TODO this shouldn't be needed
  }

  def "verify that MessageAlertStored event is sent over rabbitMQ"() {
    given:
    def conditions = new PollingConditions(timeout: 5, initialDelay: 0.2, factor: 1.25)

    def receivedMessage = null
    1 * dataPrepFacade.processAlert(_) >> { AlertMessageStored msg ->
      receivedMessage = msg
    }

    when: 'send message to queue'
    rabbitTemplate.convertAndSend(queueName, solvingMessage)

    then: 'message is received'
    noExceptionThrown()
    conditions.eventually {
      assert receivedMessage == solvingMessage
    }
  }

  def 'learningMessage should be retried'() {
    given:
    def conditions = new PollingConditions(timeout: 5, initialDelay: 0.2, factor: 1.25)

    def counter = 0
    dataPrepFacade.processAlert(_) >> {
      counter++
      throw new RuntimeException()
    }

    boolean failedCalled = false
    1 * dataPrepFacade.processAlertFailed(*_) >> {
      failedCalled = true
    }

    when: 'send message to queue'
    rabbitTemplate.convertAndSend(queueName, learningMessage)

    then: 'message is retried'
    conditions.eventually {
      assert counter == 3
      assert failedCalled
    }
  }

  def 'solvingMessage should be retried'() {
    given:
    def conditions = new PollingConditions(timeout: 5, initialDelay: 0.2, factor: 1.25)

    def counter = 0
    3 * dataPrepFacade.processAlert(_) >> {
      counter++
      throw new RuntimeException()
    }

    boolean failedCalled = false
    dataPrepFacade.processAlertFailed(*_) >> {
      failedCalled = true
    }

    when: 'send message to queue'
    rabbitTemplate.convertAndSend(queueName, solvingMessage)

    then: 'message is retried'
    conditions.eventually {
      assert counter == 3
      assert failedCalled
    }
  }
}
