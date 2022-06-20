/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.batch

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.alertrecord.AlertRecord

import spock.lang.Specification

import java.time.Instant

import static com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.domain.NeoFlag.EXISTING
import static com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.domain.NeoFlag.OBSOLETE

class AlertCompositeRowProcessorSpec extends Specification {

  def dateConverter = Mock(DateConverter)

  def objectUnderTest

  def setup() {
    dateConverter.convert(null) >> Optional.of(Instant.now())
    objectUnderTest = new AlertCompositeRowProcessor(dateConverter, true)
  }

  def "should filter obsolete and solved matches in watchlistLevel processing"() {
    given:
    def someAlertRow = createAlertRow()
    def suspectsCollection = createSuspectsCollection(suspects)
    def decisionsCollection = new DecisionsCollection([])

    when:
    List<AlertComposite> list = objectUnderTest.
        process(someAlertRow, suspectsCollection, decisionsCollection)

    then:
    list.size() == 1
    com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.alert.AlertDetails alertDetails = list.first().alert.details()
    alertDetails.batchId == 'batchId'
    alertDetails.watchlistId == 'ofacId-2'

    where:
    suspects << [
        [
            new com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.hitdetails.model.Suspect(ofacId: 'ofacId-1', batchId: 'OBSOLETE-batchId', index: 1),
            new com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.hitdetails.model.Suspect(ofacId: 'ofacId-2', batchId: 'batchId', index: 2)
        ],
        [
            new com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.hitdetails.model.Suspect(ofacId: 'ofacId-1', batchId: 'batchId1', neoFlag: OBSOLETE, index: 1),
            new com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.hitdetails.model.Suspect(ofacId: 'ofacId-2', batchId: 'batchId', index: 2)
        ],
        [
            new com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.hitdetails.model.Suspect(ofacId: 'ofacId-1', batchId: 'batchId1', neoFlag: EXISTING, index: 1),
            new com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.hitdetails.model.Suspect(ofacId: 'ofacId-2', batchId: 'batchId', index: 2)
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
