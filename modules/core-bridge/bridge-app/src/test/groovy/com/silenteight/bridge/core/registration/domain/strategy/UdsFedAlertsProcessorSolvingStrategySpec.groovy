package com.silenteight.bridge.core.registration.domain.strategy

import com.silenteight.bridge.core.registration.domain.model.Batch
import com.silenteight.bridge.core.registration.domain.model.Batch.BatchStatus
import com.silenteight.bridge.core.registration.domain.port.outgoing.AlertRepository
import com.silenteight.bridge.core.registration.domain.port.outgoing.AnalysisService
import com.silenteight.bridge.core.registration.infrastructure.RegistrationAnalysisProperties

import com.google.protobuf.Timestamp
import spock.lang.Specification
import spock.lang.Subject

import java.time.Duration

class UdsFedAlertsProcessorSolvingStrategySpec extends Specification {

  def analysisService = Mock(AnalysisService)
  def analysisProperties = new RegistrationAnalysisProperties(Duration.ofMinutes(10), false)
  def alertRepository = Mock(AlertRepository)

  @Subject
  def underTest = new UdsFedAlertsProcessorSolvingStrategy(analysisService, analysisProperties, alertRepository)

  def 'should process UDS fed alerts for SOLVING batch'() {
    given:
    def alertNames = ['alertName1, alertName2']
    def batch = Batch.builder()
        .id('batchId')
        .status(BatchStatus.COMPLETED)
        .analysisName("analysisName")
        .isSimulation(false)
        .build()

    when:
    underTest.processUdsFedAlerts(batch, alertNames)

    then:
    1 * alertRepository.updateStatusToProcessing(batch.id(), alertNames)
    1 * analysisService.addAlertsToAnalysis(batch.analysisName(), alertNames, _ as Timestamp)
    0 * _
  }

  def 'should get strategy name'() {
    when:
    def result = underTest.getStrategyName()

    then:
    result == BatchStrategyName.SOLVING
  }
}
