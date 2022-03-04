package com.silenteight.fab.dataprep.adapter.incoming

import com.silenteight.fab.dataprep.BaseSpecificationIT
import com.silenteight.fab.dataprep.domain.FeedingFacade
import com.silenteight.proto.fab.api.v1.MessageAlertAndMatchesStored
import com.silenteight.proto.fab.api.v1.StoredMatch

import org.spockframework.spring.SpringBean
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import spock.util.concurrent.PollingConditions

import static com.silenteight.fab.dataprep.adapter.incoming.AlertAndMatchesRabbitAmqpListener.QUEUE_NAME_PROPERTY

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@Import(IngestFlowRabbitMqTestConfig.class)
@ActiveProfiles("dev")
class AlertAndMatchesRabbitAmqpListenerTest extends BaseSpecificationIT {

  @Autowired
  private RabbitTemplate rabbitTemplate

  @SpringBean
  FeedingFacade feedingFacade = Mock()

  @Value(QUEUE_NAME_PROPERTY)
  String queueName

  def "verify that MessageAlertAndMatchesStored event is sent over rabbitMQ"() {
    given:
    def conditions = new PollingConditions(timeout: 5, initialDelay: 0.2, factor: 1.25)
    def message = MessageAlertAndMatchesStored.newBuilder()
        .setBatchId("batchId")
        .setAlertId("alertId")
        .setAlertName("alertName")
        .setAlertStatus("SUCCESS")
        .addStoredMatches(
            StoredMatch.newBuilder().setMatchId("matchId").setMatchName("matchName").build())
        .build()

    MessageAlertAndMatchesStored receivedMessage = null
    1 * feedingFacade.feedUds(_) >> { MessageAlertAndMatchesStored msg ->
      receivedMessage = msg
    }

    when: 'send message to queue'
    rabbitTemplate.convertAndSend(queueName, message)

    then: 'message is received'
    noExceptionThrown()
    conditions.eventually {
      assert receivedMessage == message
    }
  }
}
