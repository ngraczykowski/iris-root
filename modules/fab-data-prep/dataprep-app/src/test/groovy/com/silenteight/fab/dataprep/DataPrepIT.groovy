package com.silenteight.fab.dataprep

import com.silenteight.data.api.v2.Alert
import com.silenteight.data.api.v2.ProductionDataIndexRequest
import com.silenteight.fab.dataprep.DataPrepConfig.CoreBridgeListener
import com.silenteight.fab.dataprep.DataPrepConfig.WarehouseListener
import com.silenteight.fab.dataprep.adapter.incoming.AlertDetailsFacade
import com.silenteight.fab.dataprep.domain.AlertService
import com.silenteight.proto.fab.api.v1.AlertMessageDetails
import com.silenteight.proto.fab.api.v1.AlertMessageStored
import com.silenteight.proto.fab.api.v1.AlertMessageStored.State
import com.silenteight.proto.fab.api.v1.AlertMessagesDetailsResponse
import com.silenteight.proto.registration.api.v1.MessageAlertMatchesFeatureInputFed
import com.silenteight.proto.registration.api.v1.MessageAlertMatchesFeatureInputFed.FeedingStatus
import com.silenteight.registration.api.library.v1.*
import com.silenteight.universaldatasource.api.library.agentinput.v1.AgentInputServiceClient

import com.google.protobuf.Struct
import org.spockframework.spring.SpringBean
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.context.annotation.Import
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import spock.lang.Ignore
import spock.lang.Unroll
import spock.util.concurrent.PollingConditions

import static com.google.protobuf.Value.newBuilder
import static com.silenteight.fab.dataprep.adapter.incoming.AlertMessagesRabbitAmqpListener.QUEUE_NAME_PROPERTY
import static com.silenteight.fab.dataprep.domain.Fixtures.*

@DirtiesContext
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@ActiveProfiles("dev")
@Import(DataPrepConfig)
class DataPrepIT extends BaseSpecificationIT {

  @Autowired
  RabbitTemplate rabbitTemplate

  @Autowired
  CoreBridgeListener coreBridgeListener

  @Autowired
  WarehouseListener warehouseListener

  @Value(QUEUE_NAME_PROPERTY)
  String queueName

  @SpringBean
  AlertDetailsFacade alertDetailsFacade = Mock()

  @SpringBean
  RegistrationServiceClient registrationServiceClient = Mock()

  @SpringBean
  AgentInputServiceClient agentInputServiceClient = Mock()

  @Autowired
  AlertService alertService

  def setupSpec() {
    startPostgresql()
    startRabbitmq()
  }

  def cleanup() {
    coreBridgeListener.clear()
    warehouseListener.clear()

    alertService.deleteAll()
  }

  @Unroll
  def "messages with #expectedStatus should be sent to core-bridge"() {
    given:
    def conditions = new PollingConditions(timeout: 5, initialDelay: 0.2, factor: 1.25)
    def fedMessage = AlertMessageStored.newBuilder()
        .setBatchName(BATCH_NAME)
        .setMessageName(MESSAGE_NAME)
        .setState(State.NEW)
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
      assert msg.getAlertName() == ALERT_NAME
      assert msg.getBatchId() == BATCH_NAME
      if (expectedStatus == FeedingStatus.SUCCESS) {
        assert msg.getFedMatchesList().first().getMatchName() == MATCH_NAME
      }
    }

    where:
    expectedStatus << [FeedingStatus.SUCCESS]
  }

  @Ignore
  def "if json is not valid then FAILURE status should be sent"() {
    given:
    def conditions = new PollingConditions(timeout: 5, initialDelay: 0.2, factor: 1.25)
    def fedMessage = AlertMessageStored.newBuilder()
        .setBatchName(BATCH_NAME)
        .setMessageName(MESSAGE_NAME)
        .setState(State.NEW)
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
      assert msg.getFeedingStatus() == FeedingStatus.SUCCESS
      assert msg.getAlertName() == ALERT_NAME
      assert msg.getBatchId() == BATCH_NAME
    }
  }

  def "messages with learning data should be sent to warehouse"() {
    given:
    def conditions = new PollingConditions(timeout: 5, initialDelay: 0.2, factor: 1.25)
    def fedMessage = AlertMessageStored.newBuilder()
        .setBatchName(BATCH_NAME)
        .setMessageName(MESSAGE_NAME)
        .setState(State.SOLVED_TRUE_POSITIVE)
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

    1 * agentInputServiceClient.createBatchCreateAgentInputs(_) >> []

    when: 'send fedMessage to queue'
    rabbitTemplate.convertAndSend(queueName, fedMessage)

    then: 'output fedMessage is received'
    conditions.eventually {
      assert coreBridgeListener.getMessages().isEmpty()
      ProductionDataIndexRequest msg = warehouseListener.getMessages().last()
      assert msg.getAlertsList().first() == Alert.newBuilder()
          .setAccessPermissionTag('AE')
          .setName(ALERT_NAME)
          .setDiscriminator(DISCRIMINATOR)
          .setPayload(
              Struct.newBuilder()
                  .putAllFields(
                      ['analystDecision'                : createValue(
                          'analyst_decision_true_positive'),
                       'originalAnalystDecision'        : createValue(CURRENT_STATUS_NAME),
                       'analystDecisionModifiedDateTime': createValue(CURRENT_ACTION_DATE_TIME),
                       'analystReason'                  : createValue('')])
                  .build())
          .build()
    }
  }

  def createValue(String text) {
    newBuilder().setStringValue(text).build()
  }
}
