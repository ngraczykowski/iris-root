package com.silenteight.bridge.core.registration.domain.strategy

import com.silenteight.bridge.core.registration.domain.model.Batch
import com.silenteight.bridge.core.registration.domain.model.Batch.BatchStatus
import com.silenteight.bridge.core.registration.domain.model.SimulationBatchCompleted
import com.silenteight.bridge.core.registration.domain.port.outgoing.AlertRepository
import com.silenteight.bridge.core.registration.domain.port.outgoing.BatchEventPublisher
import com.silenteight.bridge.core.registration.domain.port.outgoing.BatchRepository

import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

class UdsFedAlertsProcessorSimulationStrategySpec extends Specification {

  def alertRepository = Mock(AlertRepository)
  def batchRepository = Mock(BatchRepository)
  def eventPublisher = Mock(BatchEventPublisher)

  @Subject
  def underTest = new UdsFedAlertsProcessorSimulationStrategy(alertRepository, batchRepository, eventPublisher)

  @Unroll
  def "should process UDS fed alerts #desc for SIMULATION batch"() {
    given:
    def alertNames = ['alertName1, alertName2']
    def batch = Batch.builder()
        .id('batchId')
        .status(BatchStatus.COMPLETED)
        .analysisName("analysisName")
        .alertsCount(batchAlertsCount)
        .isSimulation(true)
        .build()

    when:
    underTest.processUdsFedAlerts(batch, alertNames)

    then:
    1 * alertRepository.updateStatusToUdsFed(batch.id(), alertNames)
    1 * alertRepository.countAllUdsFedAndErrorAlerts(batch.id()) >> dbAlertsCount
    if (shouldCompleteBatch) {
      1 * batchRepository.updateStatusToCompleted(batch.id())
      1 * eventPublisher.publish(_ as SimulationBatchCompleted)
    }
    0 * _

    where:
    batchAlertsCount | dbAlertsCount | shouldCompleteBatch | desc
    2                | 2             | true                | 'and mark batch as COMPLETED'
    2                | 1             | false               | 'and do not mark as COMPLETED because batchAlertsCount != dbAlertsCount'
    1                | 2             | false               | 'and do not mark as COMPLETED because batchAlertsCount != dbAlertsCount'
  }

  def 'should get strategy name'() {
    when:
    def result = underTest.getStrategyName()

    then:
    result == BatchStrategyName.SIMULATION
  }
}
