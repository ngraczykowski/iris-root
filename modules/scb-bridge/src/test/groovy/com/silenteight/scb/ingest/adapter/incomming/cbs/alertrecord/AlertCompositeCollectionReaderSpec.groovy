package com.silenteight.scb.ingest.adapter.incomming.cbs.alertrecord

import com.silenteight.scb.ingest.adapter.incomming.cbs.alertid.AlertId
import com.silenteight.scb.ingest.adapter.incomming.cbs.alertmapper.AlertMapper
import com.silenteight.scb.ingest.adapter.incomming.cbs.alertrecord.InvalidAlert.Reason
import com.silenteight.scb.ingest.adapter.incomming.common.alertrecord.AlertRecord
import com.silenteight.proto.serp.scb.v1.ScbAlertIdContext

import spock.lang.Specification

class AlertCompositeCollectionReaderSpec extends Specification {

  def alertMapper = Mock(AlertMapper)
  def databaseAlertRecordCompositeReader = Mock(DatabaseAlertRecordCompositeReader)
  def underTest = new AlertCompositeCollectionReader(
      alertMapper, databaseAlertRecordCompositeReader, true)

  def fixtures = new Fixtures()

  def 'should read alert composites'() {
    given:
    def alertIds = [fixtures.alertId1, fixtures.alertId2, fixtures.alertId3]
    def alertRecords = Mock(AlertRecordCompositeCollection)
    alertRecords.getAlerts() >> [fixtures.alertRecordComposite1, fixtures.alertRecordComposite2]
    alertRecords.getInvalidAlerts() >> [fixtures.invalidAlert]

    when:
    def result = underTest.read(alertIds, fixtures.alertIdContext)

    then:
    result.invalidAlerts.size() == 2
    result.validAlerts.size() == 1

    1 * databaseAlertRecordCompositeReader.read(fixtures.alertIdContext.sourceView, alertIds) >>
        alertRecords
    1 * alertMapper.fromAlertRecordComposite(fixtures.alertRecordComposite1, _) >> []
    1 * alertMapper.fromAlertRecordComposite(fixtures.alertRecordComposite2, _) >> {
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
}
