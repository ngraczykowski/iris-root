package com.silenteight.bridge.core.recommendation.domain

import com.silenteight.bridge.core.recommendation.domain.command.ProceedBatchTimeoutCommand
import com.silenteight.bridge.core.recommendation.domain.command.ProceedReadyRecommendationsCommand
import com.silenteight.bridge.core.recommendation.domain.model.RecommendationWithMetadata
import com.silenteight.bridge.core.recommendation.domain.model.RecommendationsStoredEvent
import com.silenteight.bridge.core.recommendation.domain.port.outgoing.RecommendationEventPublisher
import com.silenteight.bridge.core.recommendation.domain.port.outgoing.RecommendationRepository

import spock.lang.Specification
import spock.lang.Subject

class RecommendationProcessorSpec extends Specification {

  def eventPublisher = Mock(RecommendationEventPublisher)
  def recommendationRepository = Mock(RecommendationRepository)

  @Subject
  def underTest = new RecommendationProcessor(
      eventPublisher,
      recommendationRepository
  )

  def 'should proceed ready recommendations without existing duplicates in DB'() {
    given:
    def recommendationWithMetadata = [RecommendationFixtures.RECOMMENDATION_WITH_METADATA]
    def analysisName = recommendationWithMetadata.first().analysisName()

    when:
    underTest.proceedReadyRecommendations(new ProceedReadyRecommendationsCommand(recommendationWithMetadata, analysisName), RecommendationFixtures.BATCH_PRIORITY)

    then:
    1 * recommendationRepository.findRecommendationAlertNamesByAnalysisName(analysisName) >> []
    1 * recommendationRepository.saveAll(_ as List<RecommendationWithMetadata>)
    1 * eventPublisher.publish(_ as RecommendationsStoredEvent)
  }

  def 'should proceed ready recommendations with existing duplicates in DB'() {
    given:
    def recommendationWithMetadata = [RecommendationFixtures.RECOMMENDATION_WITH_METADATA]
    def analysisName = recommendationWithMetadata.first().analysisName()
    def existingRecommendationAlertNames = [recommendationWithMetadata.first().alertName()]

    when:
    underTest.proceedReadyRecommendations(new ProceedReadyRecommendationsCommand(recommendationWithMetadata, analysisName), RecommendationFixtures.BATCH_PRIORITY)

    then:
    1 * recommendationRepository.findRecommendationAlertNamesByAnalysisName(analysisName) >>
        existingRecommendationAlertNames
    0 * recommendationRepository.saveAll([])
    0 * eventPublisher.publish(new RecommendationsStoredEvent(analysisName, [], false, RecommendationFixtures.BATCH_PRIORITY))
  }

  def 'should create timed out recommendations without existing duplicates in DB'() {
    given:
    def analysisName = RecommendationFixtures.ANALYSIS_NAME
    def alertNames = ['alertName1', 'alertName2']

    when:
    underTest.createTimedOutRecommendations(new ProceedBatchTimeoutCommand(analysisName, alertNames), RecommendationFixtures.BATCH_PRIORITY)

    then:
    1 * recommendationRepository.findRecommendationAlertNamesByAnalysisName(analysisName) >> []
    1 * recommendationRepository.saveAll(_ as List<RecommendationWithMetadata>)
    1 * eventPublisher.publish(new RecommendationsStoredEvent(analysisName, alertNames, true, RecommendationFixtures.BATCH_PRIORITY))
  }

  def 'should create timed out recommendations with existing duplicates in DB'() {
    given:
    def analysisName = RecommendationFixtures.ANALYSIS_NAME
    def alertNames = ['alertName1', 'alertName2']
    def existingRecommendationAlertNames = ['alertName1', 'alertName2']

    when:
    underTest.createTimedOutRecommendations(new ProceedBatchTimeoutCommand(analysisName, alertNames), RecommendationFixtures.BATCH_PRIORITY)

    then:
    1 * recommendationRepository.findRecommendationAlertNamesByAnalysisName(analysisName) >>
        existingRecommendationAlertNames
    0 * recommendationRepository.saveAll(_ as List<RecommendationWithMetadata>)
    0 * eventPublisher.publish(new RecommendationsStoredEvent(analysisName, ['alertName2'], true, RecommendationFixtures.BATCH_PRIORITY))
  }
}
