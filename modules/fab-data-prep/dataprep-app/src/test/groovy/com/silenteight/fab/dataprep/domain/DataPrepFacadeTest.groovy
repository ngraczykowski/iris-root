package com.silenteight.fab.dataprep.domain

import com.silenteight.fab.dataprep.adapter.incoming.AlertDetailsFacade
import com.silenteight.fab.dataprep.domain.ex.DataPrepException
import com.silenteight.fab.dataprep.domain.model.AlertState
import com.silenteight.fab.dataprep.domain.model.CreateAlertItem
import com.silenteight.fab.dataprep.domain.model.LearningData
import com.silenteight.proto.fab.api.v1.AlertMessageStored
import com.silenteight.proto.fab.api.v1.AlertMessageStored.State

import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import static com.silenteight.fab.dataprep.domain.Fixtures.*
import static com.silenteight.fab.dataprep.domain.model.AlertErrorDescription.GETTING_FROM_DB

@ContextConfiguration(classes = [DataPrepFacade, DataPrepConfiguration, LearningUseCase,
    SolvingUseCase],
    initializers = ConfigDataApplicationContextInitializer)
@ActiveProfiles("dev")
class DataPrepFacadeTest extends Specification {

  @SpringBean
  FeedingFacade feedingFacade = Mock()
  @SpringBean
  AlertDetailsFacade alertDetailsFacade = Mock()
  @SpringBean
  AlertParser alertParser = Mock()
  @SpringBean
  RegistrationService registrationService = Mock()
  @SpringBean
  AlertService alertService = Mock()
  @SpringBean
  LearningService learningService = Mock()
  @Autowired
  AlertStateProperties alertStateProperties

  @Subject
  @Autowired
  DataPrepFacade underTest

  def alertMessageStoredBuilder = AlertMessageStored.newBuilder()
      .setBatchName(BATCH_NAME)
      .setMessageName(MESSAGE_NAME)
      .setState(State.NEW)

  def 'messages with STATE_UNSPECIFIED status should be rejected'() {
    given:
    def message = alertMessageStoredBuilder
        .setState(State.STATE_UNSPECIFIED)
        .build()

    when:
    underTest.processAlert(message)

    then:
    thrown(DataPrepException)
    1 * alertDetailsFacade.getAlertDetails(message) >> ALERT_MESSAGES_DETAILS_RESPONSE
    1 * alertParser.parse(message, ALERT_MESSAGE_DETAILS) >> PARSED_ALERT_MESSAGE
    0 * registrationService._
    0 * feedingFacade._
    0 * alertService._
    0 * learningService._
  }

  def 'messages with NEW status should be registered'() {
    given:
    def message = alertMessageStoredBuilder.build()

    when:
    underTest.processAlert(message)

    then:
    1 * alertDetailsFacade.getAlertDetails(message) >> ALERT_MESSAGES_DETAILS_RESPONSE
    1 * alertParser.parse(message, ALERT_MESSAGE_DETAILS) >> PARSED_ALERT_MESSAGE
    1 * registrationService.registerAlertsAndMatches([(MESSAGE_NAME): PARSED_ALERT_MESSAGE]) >>
        [REGISTERED_ALERT]
    1 * feedingFacade.etlAndFeedUds(REGISTERED_ALERT)
    0 * feedingFacade._
    1 * alertService.getAlertItem(MESSAGE_NAME) >> Optional.empty()
    1 * alertService.save(
        CreateAlertItem.builder()
            .alertName(ALERT_NAME)
            .messageName(MESSAGE_NAME)
            .matchNames([MATCH_NAME])
            .build())
    1 * alertService.setAlertState(MESSAGE_NAME, AlertState.IN_UDS)
    0 * learningService._
  }

  def 'solving, registered but not in UDS should be retried'() {
    given:
    def message = alertMessageStoredBuilder.build()

    when:
    underTest.processAlert(message)

    then:
    1 * alertDetailsFacade.getAlertDetails(message) >> ALERT_MESSAGES_DETAILS_RESPONSE
    1 * alertParser.parse(message, ALERT_MESSAGE_DETAILS) >> PARSED_ALERT_MESSAGE
    0 * registrationService._
    1 * feedingFacade.etlAndFeedUds(REGISTERED_ALERT)
    1 * alertService.getAlertItem(MESSAGE_NAME) >> Optional.of(ALERT_ITEM)
    1 * alertService.setAlertState(MESSAGE_NAME, AlertState.IN_UDS)
    0 * alertService._
    0 * learningService._
  }

  def 'solving and already registered data should not be sent to UDS'() {
    given:
    def message = alertMessageStoredBuilder.build()

    when:
    underTest.processAlert(message)

    then:
    1 * alertDetailsFacade.getAlertDetails(message) >> ALERT_MESSAGES_DETAILS_RESPONSE
    1 * alertParser.parse(message, ALERT_MESSAGE_DETAILS) >> PARSED_ALERT_MESSAGE
    0 * registrationService._
    0 * feedingFacade._
    1 * alertService.getAlertItem(MESSAGE_NAME) >> Optional.of(ALERT_ITEM_IN_UDS)
    0 * alertService._
    0 * learningService._
  }

  def 'learning, registered but not in UDS should be retried'() {
    given:
    def message = alertMessageStoredBuilder
        .setState(State.SOLVED_TRUE_POSITIVE)
        .build()

    when:
    underTest.processAlert(message)

    then:
    1 * alertDetailsFacade.getAlertDetails(message) >> ALERT_MESSAGES_DETAILS_RESPONSE
    1 * alertParser.parse(message, ALERT_MESSAGE_DETAILS) >> PARSED_ALERT_MESSAGE
    0 * registrationService._
    1 * feedingFacade.etlAndFeedUds(REGISTERED_ALERT)
    2 * alertService.getAlertItem(MESSAGE_NAME) >> Optional.of(ALERT_ITEM)
    1 * alertService.setAlertState(MESSAGE_NAME, AlertState.IN_UDS)
    0 * alertService._
    1 * learningService.feedWarehouse(
        LearningData.builder()
            .alertName(ALERT_NAME)
            .discriminator(MESSAGE_NAME)
            .analystDecision('analyst_decision_true_positive')
            .accessPermissionTag('AE')
            .analystReason("")
            .analystDecisionModifiedDateTime("20180827094707")
            .originalAnalystDecision("COMMHUB")
            .build())
  }

  def 'learning and already registered data should not be sent to UDS'() {
    given:
    def message = alertMessageStoredBuilder
        .setState(State.SOLVED_TRUE_POSITIVE)
        .build()

    when:
    underTest.processAlert(message)

    then:
    1 * alertDetailsFacade.getAlertDetails(message) >> ALERT_MESSAGES_DETAILS_RESPONSE
    1 * alertParser.parse(message, ALERT_MESSAGE_DETAILS) >> PARSED_ALERT_MESSAGE
    0 * registrationService._
    0 * feedingFacade._
    2 * alertService.getAlertItem(MESSAGE_NAME) >> Optional.of(ALERT_ITEM_IN_UDS)
    0 * alertService._
    1 * learningService.feedWarehouse(
        LearningData.builder()
            .alertName(ALERT_NAME)
            .discriminator(MESSAGE_NAME)
            .analystDecision('analyst_decision_true_positive')
            .accessPermissionTag('AE')
            .analystReason("")
            .analystDecisionModifiedDateTime("20180827094707")
            .originalAnalystDecision("COMMHUB")
            .build())
  }

  @Unroll
  def 'learning and not registered data should be sent to UDS #state'() {
    given:
    def message = alertMessageStoredBuilder
        .setState(state)
        .build()

    when:
    underTest.processAlert(message)

    then:
    1 * alertDetailsFacade.getAlertDetails(message) >> ALERT_MESSAGES_DETAILS_RESPONSE
    1 * alertParser.parse(message, ALERT_MESSAGE_DETAILS) >> PARSED_ALERT_MESSAGE
    1 * registrationService.registerAlertsAndMatches([(MESSAGE_NAME): PARSED_ALERT_MESSAGE]) >>
        [REGISTERED_ALERT]
    1 * feedingFacade.etlAndFeedUds(REGISTERED_ALERT)
    0 * feedingFacade._
    2 * alertService.getAlertItem(MESSAGE_NAME) >>> [Optional.empty(), Optional.of(ALERT_ITEM)]
    1 * alertService.save(
        CreateAlertItem.builder()
            .alertName(ALERT_NAME)
            .messageName(MESSAGE_NAME)
            .matchNames([MATCH_NAME])
            .build())
    1 * alertService.setAlertState(MESSAGE_NAME, AlertState.IN_UDS)
    1 * learningService.feedWarehouse(
        LearningData.builder()
            .alertName(ALERT_NAME)
            .discriminator(MESSAGE_NAME)
            .analystDecision(analystDecision)
            .accessPermissionTag('AE')
            .analystReason("")
            .analystDecisionModifiedDateTime("20180827094707")
            .originalAnalystDecision("COMMHUB")
            .build())

    where:
    state                       | analystDecision
    State.SOLVED_TRUE_POSITIVE  | 'analyst_decision_true_positive'
    State.SOLVED_FALSE_POSITIVE | 'analyst_decision_false_positive'
  }

  def 'error during registration should be handled'() {
    given:
    def message = alertMessageStoredBuilder
        .setState(State.NEW)
        .build()

    when:
    underTest.processAlertFailed(message, new DataPrepException(GETTING_FROM_DB.getDescription()))

    then:
    1 * alertDetailsFacade.getAlertDetails(message) >> ALERT_MESSAGES_DETAILS_RESPONSE
    1 * alertParser.parse(message, ALERT_MESSAGE_DETAILS) >> PARSED_ALERT_MESSAGE
    1 * registrationService
        .registerFailedAlerts(
            [MESSAGE_NAME], BATCH_NAME, GETTING_FROM_DB) >>
        [REGISTERED_ALERT]
    1 * feedingFacade.notifyAboutError(BATCH_NAME, ALERT_NAME, GETTING_FROM_DB)
    0 * feedingFacade._
    1 * alertService.getAlertItem(MESSAGE_NAME) >> Optional.empty()
    0 * learningService._
  }

  def 'error during feeding UDS should be handled'() {
    given:
    def message = alertMessageStoredBuilder
        .setState(State.NEW)
        .build()

    when:
    underTest.processAlertFailed(message, new DataPrepException(GETTING_FROM_DB.getDescription()))

    then:
    1 * alertDetailsFacade.getAlertDetails(message) >> ALERT_MESSAGES_DETAILS_RESPONSE
    1 * alertParser.parse(message, ALERT_MESSAGE_DETAILS) >> PARSED_ALERT_MESSAGE
    0 * registrationService._
    1 * feedingFacade.notifyAboutError(BATCH_NAME, ALERT_NAME, GETTING_FROM_DB)
    0 * feedingFacade._
    1 * alertService.getAlertItem(MESSAGE_NAME) >> Optional.of(ALERT_ITEM)
    0 * learningService._
  }
}
