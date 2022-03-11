package com.silenteight.scb.ingest.adapter.incomming.common.batch.ecm

import com.silenteight.scb.ingest.adapter.incomming.common.alertrecord.AlertRecord
import com.silenteight.scb.ingest.adapter.incomming.common.batch.DateConverter
import com.silenteight.scb.ingest.adapter.incomming.common.batch.SuspectsCollection
import com.silenteight.scb.ingest.adapter.incomming.common.hitdetails.model.Suspect
import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.AlertDetails
import com.silenteight.scb.ingest.adapter.incomming.common.model.decision.Decision

import spock.lang.Specification

import java.time.Instant

class EcmAlertCompositeRowProcessorSpec extends Specification {

  DateConverter dateConverter = Mock(DateConverter)

  EcmAlertCompositeRowProcessor objectUnderTest

  def setup() {
    dateConverter.convert(null) >> Optional.of(Instant.now())
    objectUnderTest = new EcmAlertCompositeRowProcessor(dateConverter)
  }

  def "should process ecm alert composite"() {
    given:
    def someAlertRow = createAlertRow()
    def suspectsCollection = createSuspectsCollection(suspects)
    def externalId = new ExternalId('system-id', 'AM00000965')
    def decisions = [:]
    decisions.put(externalId, [Decision.builder().build()])

    when:
    def list = objectUnderTest.process(someAlertRow, decisions, suspectsCollection)

    then:
    list.size() == 1
    def alertComposite = list.first()
    alertComposite.decisionsCount == 1
    AlertDetails alertDetails = alertComposite.alert.details
    alertDetails.batchId == 'batchId'
    alertDetails.watchlistId == 'AM00000965'

    where:
    suspects << [
        [
            new Suspect(ofacId: 'AM00000965', batchId: 'OBSOLETE-batchId', index: 1),
            new Suspect(ofacId: 'ofacId-2', batchId: 'batchId', index: 2)
        ]
    ]
  }

  def createSuspectsCollection(suspects) {
    new SuspectsCollection(suspects)
  }

  def createAlertRow() {
    AlertRecord.builder()
        .details('details')
        .systemId('system-id')
        .batchId('batchId')
        .fmtName('SCB_EDMP_DUED')
        .charSep('c' as char)
        .record('a~b')
        .unit('EN_BTCH_ALLOW')
        .build()
  }
}
