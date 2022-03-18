package com.silenteight.scb.ingest.adapter.incomming.common.batch

import com.silenteight.scb.ingest.adapter.incomming.cbs.gateway.CbsAckGateway
import com.silenteight.scb.ingest.adapter.incomming.common.domain.GnsSyncDeltaService
import com.silenteight.scb.ingest.adapter.incomming.common.ingest.BatchAlertIngestService
import com.silenteight.scb.ingest.adapter.incomming.common.model.ObjectId
import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert
import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.AlertDetails

import io.micrometer.core.instrument.Tag
import spock.lang.Specification
import spock.lang.Unroll

import java.util.stream.Stream

class RecordCompositeWriterSpec extends Specification {

  def deltaService = Mock(GnsSyncDeltaService)
  def ingestService = Mock(BatchAlertIngestService)
  def cbsAckGateway = Mock(CbsAckGateway)
  def deltaJobName = "DELTA_NAME"

  @Unroll
  def "should write alerts for learning, useDelta=#useDelta, ackRecords=#ackRecords"() {
    given:
    when:
    createRecordWriter().write([])

    then:
    1 * ingestService.ingestAlertsForLearn(_ as Stream)
    1 * deltaService.updateDelta(_ as Map, deltaJobName)
  }

  def 'should merge duplicated alerts for delta update'() {
    given:
    def underTest = createRecordWriter()
    def alerts = [createAlertComposite(), createAlertComposite()]

    when:
    underTest.write(alerts)

    then:
    1 * deltaService.updateDelta({Map map -> map.size() == 1}, deltaJobName)
  }

  def createAlert() {
    Alert.builder()
        .id(createId())
        .details(createAlertDetails())
        .build()
  }

  def createAlertDetails() {
    AlertDetails
        .builder()
        .batchId('batch-id')
        .systemId('system-id')
        .build()
  }

  def createId() {
    ObjectId.builder().sourceId('external-id').build()
  }

  def createAlertComposite() {
    new AlertComposite(createAlert(), 0, [Mock(Tag)])
  }

  def createRecordWriter() {
    new RecordCompositeWriter(
        deltaService, ingestService, cbsAckGateway, true, deltaJobName)
  }
}
