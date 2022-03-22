package com.silenteight.fab.dataprep

import com.silenteight.fab.dataprep.DataPrepConfig.CoreBridgeListener
import com.silenteight.fab.dataprep.adapter.incoming.AlertDetailsFacade
import com.silenteight.proto.fab.api.v1.AlertMessageDetails
import com.silenteight.proto.fab.api.v1.AlertMessageStored
import com.silenteight.proto.fab.api.v1.AlertMessagesDetailsResponse
import com.silenteight.proto.registration.api.v1.MessageAlertMatchesFeatureInputFed
import com.silenteight.proto.registration.api.v1.MessageAlertMatchesFeatureInputFed.FeedingStatus
import com.silenteight.registration.api.library.v1.*
import com.silenteight.universaldatasource.api.library.agentinput.v1.AgentInputServiceClient

import org.spockframework.spring.SpringBean
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import spock.lang.Unroll
import spock.util.concurrent.PollingConditions

import static com.silenteight.fab.dataprep.adapter.incoming.AlertMessagesRabbitAmqpListener.QUEUE_NAME_PROPERTY
import static com.silenteight.fab.dataprep.domain.Fixtures.*

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@ActiveProfiles("dev")
@Import(DataPrepConfig.class)
class DataPrepIT extends BaseSpecificationIT {

  @Autowired
  RabbitTemplate rabbitTemplate

  @Autowired
  CoreBridgeListener coreBridgeListener

  @Value(QUEUE_NAME_PROPERTY)
  String queueName

  @SpringBean
  AlertDetailsFacade alertDetailsFacade = Mock()

  @SpringBean
  RegistrationServiceClient registrationServiceClient = Mock()

  @SpringBean
  AgentInputServiceClient agentInputServiceClient = Mock()

  def cleanup() {
    coreBridgeListener.clear()
  }

  @Unroll
  def "messages with #expectedStatus should be sent to core-bridge"() {
    given:
    def conditions = new PollingConditions(timeout: 5, initialDelay: 0.2, factor: 1.25)
    def fedMessage = AlertMessageStored.newBuilder()
        .setBatchName(BATCH_NAME)
        .setMessageName(MESSAGE_NAME)
        .build()
    def alertMessagesDetailsResponse = AlertMessagesDetailsResponse.newBuilder()
        .addAlerts(
            AlertMessageDetails.newBuilder()
                .setMessageName(MESSAGE_NAME)
                .setPayload(MESSAGE)
                .build())
        .build()

    alertDetailsFacade.getAlertDetails(_) >> alertMessagesDetailsResponse

    1 * registrationServiceClient.registerAlertsAndMatches(_) >>
        {RegisterAlertsAndMatchesIn request ->
          def matchId = request.getAlertsWithMatches().matches.first().first().getMatchId()
          RegisterAlertsAndMatchesOut.builder()
              .registeredAlertWithMatches(
                  [RegisteredAlertWithMatchesOut.builder()
                       .alertName(ALERT_NAME)
                       .alertId(MESSAGE_NAME)
                       .alertStatus(AlertStatusOut.SUCCESS)
                       .registeredMatches(
                           [
                               RegisteredMatchOut.builder()
                                   .matchId(matchId)
                                   .matchName(MATCH_NAME)
                                   .build()])
                       .build()])
              .build()
        }

    agentInputServiceClient.createBatchCreateAgentInputs(_) >> {
      if (expectedStatus == FeedingStatus.FAILURE) {
        throw new RuntimeException()
      } else {
        []
      }
    }

    when: 'send fedMessage to queue'
    rabbitTemplate.convertAndSend(queueName, fedMessage)

    then: 'output fedMessage is received'
    conditions.eventually {
      MessageAlertMatchesFeatureInputFed msg = coreBridgeListener.getMessages().last()
      assert msg.getFeedingStatus() == expectedStatus
      assert msg.getAlertId() == ALERT_NAME
      assert msg.getBatchId() == BATCH_NAME
      assert msg.getFedMatchesList().first().getMatchId() == MATCH_NAME
    }

    where:
    expectedStatus << [FeedingStatus.SUCCESS, FeedingStatus.FAILURE]
  }

  def "if json is not valid then FAILURE status should be sent"() {
    given:
    def conditions = new PollingConditions(timeout: 5, initialDelay: 0.2, factor: 1.25)
    def fedMessage = AlertMessageStored.newBuilder()
        .setBatchName(BATCH_NAME)
        .setMessageName(MESSAGE_NAME)
        .build()
    def alertMessagesDetailsResponse = AlertMessagesDetailsResponse.newBuilder()
        .addAlerts(
            AlertMessageDetails.newBuilder()
                .setMessageName(MESSAGE_NAME)
                .setPayload('invalid payload')
                .build())
        .build()

    alertDetailsFacade.getAlertDetails(_) >> alertMessagesDetailsResponse

    1 * registrationServiceClient.registerAlertsAndMatches(_) >>
        {RegisterAlertsAndMatchesIn request ->
          assert request.getAlertsWithMatches().first().getStatus() == AlertStatusIn.FAILURE
          RegisterAlertsAndMatchesOut.builder()
              .registeredAlertWithMatches(
                  [RegisteredAlertWithMatchesOut.builder()
                       .alertName(ALERT_NAME)
                       .alertId(MESSAGE_NAME)
                       .alertStatus(AlertStatusOut.FAILURE)
                       .build()])
              .build()
        }

    when: 'send fedMessage to queue'
    rabbitTemplate.convertAndSend(queueName, fedMessage)

    then: 'output fedMessage is received'
    conditions.eventually {
      MessageAlertMatchesFeatureInputFed msg = coreBridgeListener.getMessages().last()
      assert msg.getFeedingStatus() == FeedingStatus.FAILURE
      assert msg.getAlertId() == ALERT_NAME
      assert msg.getBatchId() == BATCH_NAME
    }
  }
}
