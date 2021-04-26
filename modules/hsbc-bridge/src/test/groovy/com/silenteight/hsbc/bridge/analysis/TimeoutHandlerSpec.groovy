package com.silenteight.hsbc.bridge.analysis

import com.silenteight.hsbc.bridge.analysis.dto.GetAnalysisResponseDto
import com.silenteight.hsbc.bridge.analysis.dto.GetRecommendationsDto
import com.silenteight.hsbc.bridge.analysis.event.AnalysisTimeoutEvent
import com.silenteight.hsbc.bridge.recommendation.RecommendationDto
import com.silenteight.hsbc.bridge.recommendation.event.NewRecommendationEvent

import org.springframework.context.ApplicationEventPublisher
import spock.lang.Specification

class TimeoutHandlerSpec extends Specification {

  def analysisServiceApi = Mock(AnalysisServiceApi)
  def repository = Mock(AnalysisRepository)
  def eventPublisher = Mock(ApplicationEventPublisher)
  def underTest = new TimeoutHandler(
      analysisServiceApi, repository, eventPublisher)

  def 'should process analysis after timeout'() {
    given:
    def completedAnalysis = new AnalysisEntity(id: 1L, name: 'completed')
    def inCompletedAnalysis = new AnalysisEntity(id: 2L, name: 'inCompleted')

    def completedAnalysisDto = GetAnalysisResponseDto.builder().pendingAlerts(0).build()
    def inCompletedAnalysisDto = GetAnalysisResponseDto.builder().pendingAlerts(1).build()

    when:
    underTest.process()

    then:
    1 * repository.findByTimeoutAtBeforeAndStatus(_, _) >> [completedAnalysis, inCompletedAnalysis]

    1 * analysisServiceApi.getAnalysis(completedAnalysis.name) >> completedAnalysisDto
    1 * analysisServiceApi.getAnalysis(inCompletedAnalysis.name) >> inCompletedAnalysisDto
    1 * analysisServiceApi.getRecommendations(
        {GetRecommendationsDto request -> request.analysis == inCompletedAnalysis.name}) >>
        [createRecommendation()]

    1 * eventPublisher.publishEvent(_ as NewRecommendationEvent)
    1 * eventPublisher.publishEvent(_ as AnalysisTimeoutEvent)
  }

  def createRecommendation() {
    RecommendationDto.builder()
        .alert('someAlert')
        .name('recommendation')
        .recommendedAction('action')
        .recommendationComment('comment')
        .build()
  }
}
