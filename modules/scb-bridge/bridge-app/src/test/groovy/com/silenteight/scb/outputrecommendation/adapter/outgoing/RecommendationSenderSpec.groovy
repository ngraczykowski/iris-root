package com.silenteight.scb.outputrecommendation.adapter.outgoing

import com.silenteight.scb.ingest.adapter.incomming.common.recommendation.ScbRecommendationService
import com.silenteight.scb.ingest.adapter.incomming.gnsrt.recommendation.GnsRtRecommendationService
import com.silenteight.scb.outputrecommendation.domain.Fixtures
import com.silenteight.scb.outputrecommendation.domain.model.BatchMetadata
import com.silenteight.scb.outputrecommendation.domain.model.BatchSource
import com.silenteight.scb.outputrecommendation.domain.model.ErrorRecommendationsGeneratedEvent
import com.silenteight.scb.outputrecommendation.domain.model.RecommendationsGeneratedEvent

import spock.lang.Specification
import spock.lang.Subject

class RecommendationSenderSpec extends Specification {

  def cbsRecommendationService = Mock(CbsRecommendationService)
  def gnsRtRecommendationService = Mock(GnsRtRecommendationService)
  def scbRecommendationService = Mock(ScbRecommendationService)

  @Subject
  def underTest = new RecommendationSender(
      cbsRecommendationService,
      gnsRtRecommendationService,
      scbRecommendationService)

  def 'should notify GNS-RT recommendation service after RecommendationsGeneratedEvent event'() {
    given:
    def event = recommendationsGeneratedEvent(BatchSource.GNS_RT)

    when:
    underTest.publishCompleted(event)

    then:
    1 * gnsRtRecommendationService.recommend(Fixtures.RECOMMENDATION_LIST)
    1 * scbRecommendationService.saveRecommendations(Fixtures.RECOMMENDATION_LIST)
    0 * _
  }

  def 'should notify SCB recommendation service after RecommendationsGeneratedEvent event'() {
    given:
    def event = recommendationsGeneratedEvent(BatchSource.CBS)

    when:
    underTest.publishCompleted(event)

    then:
    1 * cbsRecommendationService.recommend(Fixtures.RECOMMENDATION_LIST)
    1 * scbRecommendationService.saveRecommendations(Fixtures.RECOMMENDATION_LIST)
    0 * _
  }

  def 'should handle unrecognized batchSource in RecommendationsGeneratedEvent event'() {
    given:
    def event = recommendationsGeneratedEvent(BatchSource.LEARNING)

    when:
    underTest.publishCompleted(event)

    then:
    def e = thrown(IllegalStateException)
    e.message == 'Unrecognized BatchSource type LEARNING in batchMetadata'
  }

  def 'should notify GNS-RT recommendation service after ErrorRecommendationsGeneratedEvent event'() {
    given:
    def event = errorRecommendationsGeneratedEvent(BatchSource.GNS_RT)

    when:
    underTest.publishError(event)

    then:
    1 * gnsRtRecommendationService.batchFailed('batchId', 'some error')
    0 * _
  }

  def 'should handle unrecognized batchSource in ErrorRecommendationsGeneratedEvent event '() {
    given:
    def event = errorRecommendationsGeneratedEvent(BatchSource.LEARNING)

    when:
    underTest.publishError(event)

    then:
    def e = thrown(IllegalStateException)
    e.message == 'Unrecognized BatchSource type LEARNING in batchMetadata'
  }

  static errorRecommendationsGeneratedEvent(BatchSource batchSource) {
    ErrorRecommendationsGeneratedEvent.builder()
        .batchId('batchId')
        .errorDescription('some error')
        .batchMetadata(new BatchMetadata(batchSource))
        .build()
  }

  static recommendationsGeneratedEvent(BatchSource batchSource) {
    RecommendationsGeneratedEvent.builder()
        .batchId('batchId')
        .recommendations(Fixtures.RECOMMENDATION_LIST)
        .batchMetadata(new BatchMetadata(batchSource))
        .build()
  }
}
