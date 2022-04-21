package com.silenteight.scb.ingest.adapter.incomming.cbs.alertrecord

import com.silenteight.proto.serp.scb.v1.ScbAlertIdContext
import com.silenteight.scb.ingest.adapter.incomming.cbs.alertid.AlertId
import com.silenteight.scb.ingest.adapter.incomming.cbs.alertid.AlertIdWithDetails
import com.silenteight.scb.ingest.adapter.incomming.cbs.alertunderprocessing.AlertInFlightService
import com.silenteight.scb.ingest.adapter.incomming.common.store.batchinfo.BatchInfoService
import com.silenteight.scb.ingest.adapter.incomming.common.trafficmanagement.TrafficManager

import spock.lang.Specification

class AlertProcessorSpec extends Specification {

  def alertInFlightService = Mock(AlertInFlightService)
  def alertCompositeCollectionReader = Mock(AlertCompositeCollectionReader)
  def alertHandler = Mock(AlertHandler)
  def batchInfoService = Mock(BatchInfoService)
  def trafficManager = Mock(TrafficManager)

  def underTest = AlertProcessor.builder()
      .alertHandler(alertHandler)
      .alertInFlightService(alertInFlightService)
      .alertCompositeCollectionReader(alertCompositeCollectionReader)
      .batchInfoService(batchInfoService)
      .trafficManager(trafficManager)
      .build()

  def fixtures = new Fixtures()

  def 'should process alerts'() {
    when:
    underTest.process()

    then:
    1 * trafficManager.holdPeriodicAlertProcessing() >> false
    1 * alertInFlightService.readChunk() >> fixtures.chunkOfAlertIds
    1 * alertCompositeCollectionReader
        .read(_ as String, fixtures.alertIdContext1, [fixtures.alertId1]) >>
        fixtures.alertCompositeCollection
    1 * alertCompositeCollectionReader
        .read(_ as String, fixtures.alertIdContext2, [fixtures.alertId2]) >>
        fixtures.alertCompositeCollection
    1 * alertHandler
        .handleAlerts(_ as String, fixtures.alertIdContext1, fixtures.alertCompositeCollection)
    1 * alertHandler
        .handleAlerts(_ as String, fixtures.alertIdContext2, fixtures.alertCompositeCollection)
  }

  def "Should not process alerts when batch traffic is paused"(){
    when:
    underTest.process()

    then:
    1 * trafficManager.holdPeriodicAlertProcessing() >> true
    0 * _
  }

  class Fixtures {

    ScbAlertIdContext alertIdContext1 = ScbAlertIdContext.newBuilder().setPriority(1).build()
    ScbAlertIdContext alertIdContext2 = ScbAlertIdContext.newBuilder().setPriority(10).build()

    AlertId alertId1 = AlertId.builder()
        .systemId('systemId-1')
        .batchId('batchId-1')
        .build()
    AlertId alertId2 = AlertId.builder()
        .systemId('systemId-2')
        .batchId('batchId-2')
        .build()

    AlertIdWithDetails alertIdWithDetails1 = AlertIdWithDetails.builder()
        .systemId(alertId1.systemId)
        .batchId(alertId1.batchId)
        .context(alertIdContext1)
        .build()

    AlertIdWithDetails alertIdWithDetails2 = AlertIdWithDetails.builder()
        .systemId(alertId2.systemId)
        .batchId(alertId2.batchId)
        .context(alertIdContext2)
        .build()

    AlertCompositeCollection alertCompositeCollection = new AlertCompositeCollection([], [])

    List<AlertIdWithDetails> chunkOfAlertIds = [alertIdWithDetails1, alertIdWithDetails2]
  }
}
