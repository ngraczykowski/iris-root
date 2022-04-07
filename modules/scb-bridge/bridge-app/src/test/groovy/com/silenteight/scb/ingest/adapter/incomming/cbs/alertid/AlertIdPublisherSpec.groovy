package com.silenteight.scb.ingest.adapter.incomming.cbs.alertid

import com.silenteight.proto.serp.scb.v1.ScbAlertIdContext
import com.silenteight.scb.ingest.adapter.incomming.cbs.alertunderprocessing.AlertInFlightService
import com.silenteight.scb.ingest.adapter.incomming.common.store.batchinfo.BatchInfoService
import com.silenteight.scb.ingest.domain.model.BatchSource
import com.silenteight.scb.ingest.domain.model.IngestBatchMessage
import com.silenteight.scb.ingest.domain.port.outgoing.IngestBatchEventPublisher

import spock.lang.Specification

class AlertIdPublisherSpec extends Specification {

  def alertInFlightService = Mock(AlertInFlightService)
  def ingestBatchEventPublisher = Mock(IngestBatchEventPublisher)
  def batchInfoService = Mock(BatchInfoService)
  def objectUnderTest = AlertIdPublisher.builder()
      .alertInFlightService(alertInFlightService)
      .ingestBatchEventPublisher(ingestBatchEventPublisher)
      .batchInfoService(batchInfoService)
      .build()

  def 'should consume alertIdCollection'() {
    given:
    def context = createContext()
    def someAlertId = createAlertId('systemId-1')
    def someOtherAlertId = createAlertId('systemId-2')
    def someAlertIds = [someAlertId, someOtherAlertId]

    when:
    objectUnderTest.accept(new AlertIdCollection(someAlertIds, context))

    then:
    1 * alertInFlightService.saveUniqueAlerts(someAlertIds, _ as String, _ as ScbAlertIdContext)
        >> 2
    1 * batchInfoService.store(_ as String, BatchSource.CBS, 2)
    1 * ingestBatchEventPublisher.publish(_ as IngestBatchMessage)
  }

  def createAlertId(String systemId) {
    AlertId.builder()
        .systemId(systemId)
        .batchId('batchId')
        .build()
  }

  def createContext() {
    AlertIdContext.builder()
        .ackRecords(true)
        .hitDetailsView("CBS_VIEW")
        .recordsView("VIEW")
        .priority(10)
        .watchlistLevel(true)
        .build()
  }
}
