/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.alertrecord

import com.silenteight.proto.serp.scb.v1.ScbAlertIdContext
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.alertid.AlertId
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.alertmapper.AlertMapper
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.alertrecord.InvalidAlert.Reason
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.metrics.CbsOracleMetrics
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.alertrecord.AlertRecord
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.util.InternalBatchIdGenerator

import io.micrometer.core.instrument.simple.SimpleMeterRegistry
import spock.lang.Specification

class AlertCompositeCollectionReaderSpec extends Specification {

  def alertMapper = Mock(AlertMapper)
  def databaseAlertRecordCompositeReader = Mock(DatabaseAlertRecordCompositeReader)
  def underTest = new AlertCompositeCollectionReader(
      alertMapper, databaseAlertRecordCompositeReader, true, cbsOracleMetrics())

  def fixtures = new Fixtures()

  def 'should read alert composites'() {
    given:
    def internalBatchId = InternalBatchIdGenerator.generate()
    def alertIds = [fixtures.alertId1, fixtures.alertId2, fixtures.alertId3]
    def alertRecords = Mock(AlertRecordCompositeCollection)
    alertRecords.getAlerts() >> [fixtures.alertRecordComposite1, fixtures.alertRecordComposite2]
    alertRecords.getInvalidAlerts() >> [fixtures.invalidAlert]

    when:
    def result = underTest.read(internalBatchId, fixtures.alertIdContext, alertIds)

    then:
    result.invalidAlerts.size() == 2
    result.validAlerts.size() == 1

    1 * databaseAlertRecordCompositeReader.read(fixtures.alertIdContext, alertIds) >>
        alertRecords
    1 * alertMapper.fromAlertRecordComposite(fixtures.alertRecordComposite1, internalBatchId, _) >>
        []
    1 * alertMapper.fromAlertRecordComposite(fixtures.alertRecordComposite2, internalBatchId, _) >>
        {
          throw new RuntimeException()
        }
  }

  class Fixtures {

    String systemId1 = 'systemId1'
    String systemId2 = 'systemId2'
    String systemId3 = 'systemId3'
    String batchId = 'batchId'

    ScbAlertIdContext alertIdContext = ScbAlertIdContext.newBuilder().setSourceView('test').build()

    AlertId alertId1 = new AlertId(systemId1, batchId)
    AlertId alertId2 = new AlertId(systemId2, batchId)
    AlertId alertId3 = new AlertId(systemId3, batchId)

    AlertRecord alertRecord1 = AlertRecord.builder()
        .systemId(systemId1)
        .build()
    AlertRecord alertRecord2 = AlertRecord.builder()
        .systemId(systemId2)
        .build()

    AlertRecordComposite alertRecordComposite1 = AlertRecordComposite.builder()
        .alert(alertRecord1)
        .cbsHitDetails([])
        .decisions([])
        .build()
    AlertRecordComposite alertRecordComposite2 = AlertRecordComposite.builder()
        .alert(alertRecord2)
        .cbsHitDetails([])
        .decisions([])
        .build()

    InvalidAlert invalidAlert = new InvalidAlert(systemId3, batchId, Reason.FAILED_TO_FETCH)
  }

  static cbsOracleMetrics() {
    def cbsOracleMetrics = new CbsOracleMetrics()
    cbsOracleMetrics.bindTo(new SimpleMeterRegistry())
    cbsOracleMetrics
  }
}
