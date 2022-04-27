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

  @Subject
  def underTest = new UdsFedAlertsProcessorSimulationStrategy(alertRepository)

  def "should process UDS fed alerts for SIMULATION batch"() {
    given:
    def alertNames = ['alertName1, alertName2']
    def batch = Batch.builder()
        .id('batchId')
        .status(BatchStatus.PROCESSING)
        .analysisName("analysisName")
        .alertsCount(10)
        .isSimulation(true)
        .build()

    when:
    underTest.processUdsFedAlerts(batch, alertNames)

    then:
    1 * alertRepository.updateStatusToUdsFed(batch.id(), alertNames)
    0 * _
  }

  def 'should get strategy name'() {
    when:
    def result = underTest.getStrategyName()

    then:
    result == BatchStrategyName.SIMULATION
  }
}
