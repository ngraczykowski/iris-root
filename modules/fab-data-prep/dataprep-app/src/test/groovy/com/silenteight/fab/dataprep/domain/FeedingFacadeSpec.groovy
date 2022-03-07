package com.silenteight.fab.dataprep.domain

import com.silenteight.fab.dataprep.domain.feature.FeatureInputsCommand
import com.silenteight.fab.dataprep.domain.model.AlertErrorDescription
import com.silenteight.fab.dataprep.domain.model.AlertStatus
import com.silenteight.fab.dataprep.domain.model.ExtractedAlert
import com.silenteight.fab.dataprep.domain.model.ExtractedAlert.Match
import com.silenteight.fab.dataprep.domain.model.UdsFedEvent
import com.silenteight.fab.dataprep.domain.model.UdsFedEvent.FedMatch
import com.silenteight.fab.dataprep.domain.model.UdsFedEvent.Status
import com.silenteight.fab.dataprep.domain.outgoing.FeedingEventPublisher

import spock.lang.Specification
import spock.lang.Subject

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
    def extractedAlert = ExtractedAlert.builder()
        .batchId('batchId')
        .alertId('alertId')
        .alertName('alertName')
        .status(status)
        .errorDescription(errorDescription)
        .matches(
            [
                Match.builder()
                    .matchId('matchId')
                    .matchName('matchName')
                    .build()
            ]
        )
        .build()

    def featureInputsCommand = FeatureInputsCommand.builder()
        .batchId('batchId')
        .extractedAlert(extractedAlert)
        .build()

    def udsFedEvent = UdsFedEvent.builder()
        .batchId('batchId')
        .alertId('alertId')
        .errorDescription(errorDescription)
        .feedingStatus(feedingStatus)
        .fedMatches(
            [
                new FedMatch('matchId')
            ]
        )
        .build()

    when:
    underTest.etlAndFeedUds(extractedAlert)

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
}
