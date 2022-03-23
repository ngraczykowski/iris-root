package com.silenteight.scb.ingest.adapter.incomming.cbs.alertrecord

import com.silenteight.proto.serp.scb.v1.ScbAlertIdContext
import com.silenteight.scb.ingest.adapter.incomming.cbs.alertid.AlertId
import com.silenteight.scb.ingest.adapter.incomming.cbs.alertid.AlertIdWithDetails
import com.silenteight.scb.ingest.adapter.incomming.cbs.alertunderprocessing.AlertInFlightService
import com.silenteight.scb.ingest.adapter.incomming.cbs.batch.BatchReadEvent

import spock.lang.Specification

class AlertProcessorSpec extends Specification {

  def alertInFlightService = Mock(AlertInFlightService)
  def alertCompositeCollectionReader = Mock(AlertCompositeCollectionReader)
  def alertHandler = Mock(AlertHandler)
  def underTest = new BatchProcessingEventListener(
      alertInFlightService, alertCompositeCollectionReader, alertHandler)

  def fixtures = new Fixtures()

  def 'should process alerts'() {
    given:
    BatchReadEvent batchReadEvent = new BatchReadEvent(UUID.randomUUID().toString());

    when:
    underTest.subscribe(batchReadEvent)

    then:
    1 * alertInFlightService.getAlertsFromBatch(batchReadEvent.internalBatchId()) >>
        fixtures.chunkOfAlertIds
    1 * alertCompositeCollectionReader.read([fixtures.alertId1], fixtures.alertIdContext1) >>
        fixtures.alertCompositeCollection
    1 * alertCompositeCollectionReader.read([fixtures.alertId2], fixtures.alertIdContext2) >>
        fixtures.alertCompositeCollection
    1 * alertHandler
        .handleAlerts(batchReadEvent.internalBatchId(), fixtures.alertCompositeCollections)
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

    AlertIdWithDetails alertIdWithDetails1 = new AlertIdWithDetails(
        alertId1.systemId, alertId1.batchId, alertIdContext1)
    AlertIdWithDetails alertIdWithDetails2 = new AlertIdWithDetails(
        alertId2.systemId, alertId2.batchId, alertIdContext2)

    AlertCompositeCollection alertCompositeCollection = new AlertCompositeCollection([], [])

    List<AlertCompositeCollection> alertCompositeCollections = List
        .of(alertCompositeCollection, alertCompositeCollection);

    List<AlertIdWithDetails> chunkOfAlertIds = [alertIdWithDetails1, alertIdWithDetails2]
  }
}
