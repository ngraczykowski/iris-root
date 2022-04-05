package com.silenteight.fab.dataprep.domain

import com.silenteight.fab.dataprep.domain.feature.FeatureInputsCommand
import com.silenteight.fab.dataprep.domain.model.AlertErrorDescription
import com.silenteight.fab.dataprep.domain.model.AlertStatus
import com.silenteight.fab.dataprep.domain.model.RegisteredAlert
import com.silenteight.fab.dataprep.domain.model.RegisteredAlert.Match
import com.silenteight.fab.dataprep.domain.model.UdsFedEvent
import com.silenteight.fab.dataprep.domain.model.UdsFedEvent.FedMatch
import com.silenteight.fab.dataprep.domain.model.UdsFedEvent.Status
import com.silenteight.fab.dataprep.domain.outgoing.FeedingEventPublisher

import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import static com.silenteight.fab.dataprep.domain.Fixtures.*

class FeedingFacadeSpec extends Specification {

  FeedingService feedingService = Mock()
  FeedingEventPublisher feedingEventPublisher = Mock()

  @Subject
  def underTest = new FeedingFacade(
      feedingService,
      feedingEventPublisher
  )

  def "should get extracted alert"() {
    given:
    def registeredAlert = RegisteredAlert.builder()
        .batchName(BATCH_NAME)
        .messageName(MESSAGE_NAME)
        .alertName(ALERT_NAME)
        .status(status)
        .errorDescription(errorDescription)
        .matches(
            [
                Match.builder()
                    .hitName(HIT_NAME)
                    .matchName(MATCH_NAME)
                    .build()
            ]
        )
        .build()

    def featureInputsCommand = FeatureInputsCommand.builder()
        .registeredAlert(registeredAlert)
        .build()

    def udsFedEvent = UdsFedEvent.builder()
        .batchId(BATCH_NAME)
        .alertId(ALERT_NAME)
        .errorDescription(errorDescription)
        .feedingStatus(feedingStatus)
        .fedMatches(
            [
                new FedMatch(MATCH_NAME)
            ]
        )
        .build()

    when:
    underTest.etlAndFeedUds(registeredAlert)

    then:
    if (status == AlertStatus.SUCCESS && feedingStatus == Status.SUCCESS) {
      1 * feedingService.createFeatureInputs(featureInputsCommand)
      1 * feedingEventPublisher.publish(udsFedEvent)
      0 * _
    } else if (status == AlertStatus.SUCCESS && feedingStatus == Status.FAILURE) {
      1 * feedingService.createFeatureInputs(featureInputsCommand) >> {throw new RuntimeException()}
      1 * feedingEventPublisher.publish(udsFedEvent)
      0 * _
    } else if (AlertStatus.FAILURE) {
      1 * feedingEventPublisher.publish(udsFedEvent)
      0 * feedingService.createFeatureInputs(_)
      0 * _
    }

    where:
    status              | feedingStatus  | errorDescription
    AlertStatus.SUCCESS | Status.SUCCESS | AlertErrorDescription.NONE
    AlertStatus.SUCCESS | Status.FAILURE | AlertErrorDescription.CREATE_FEATURE_INPUT
    AlertStatus.FAILURE | Status.FAILURE | AlertErrorDescription.EXTRACTION
  }

  @Unroll
  def 'event should not be sent for learning data #status'() {
    given:
    def registeredAlert = RegisteredAlert.builder()
        .batchName(BATCH_NAME)
        .messageName(MESSAGE_NAME)
        .alertName(ALERT_NAME)
        .status(status)
        .matches(
            [
                Match.builder()
                    .hitName(HIT_NAME)
                    .matchName(MATCH_NAME)
                    .build()
            ]
        )
        .build()

    def featureInputsCommand = FeatureInputsCommand.builder()
        .registeredAlert(registeredAlert)
        .build()

    when:
    underTest.etlAndFeedUdsLearningData(registeredAlert)

    then:
    if (status == AlertStatus.SUCCESS) {
      1 * feedingService.createFeatureInputs(featureInputsCommand)
    } else {
      0 * feedingService._
    }
    0 * feedingEventPublisher._

    where:
    status << AlertStatus.values()
  }
}
