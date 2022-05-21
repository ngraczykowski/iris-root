package com.silenteight.hsbc.bridge.analysis

import com.silenteight.hsbc.bridge.analysis.dto.GetAnalysisResponseDto
import com.silenteight.hsbc.bridge.analysis.event.AnalysisCompletedEvent
import com.silenteight.hsbc.bridge.analysis.event.AnalysisTimeoutEvent
import com.silenteight.hsbc.bridge.recommendation.event.RecommendationsGeneratedEvent

import org.springframework.context.ApplicationEventPublisher
import spock.lang.Specification

class TimeoutHandlerSpec extends Specification {

  def analysisServiceClient = Mock(AnalysisServiceClient)
  def repository = Mock(AnalysisRepository)
  def eventPublisher = Mock(ApplicationEventPublisher)
  def underTest = new TimeoutHandler(analysisServiceClient, repository, eventPublisher)

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

    1 * analysisServiceClient.getAnalysis(completedAnalysis.name) >> completedAnalysisDto
    1 * analysisServiceClient.getAnalysis(inCompletedAnalysis.name) >> inCompletedAnalysisDto

    1 * eventPublisher.publishEvent(_ as AnalysisCompletedEvent)
    1 * eventPublisher.publishEvent(_ as AnalysisTimeoutEvent)
  }
}
