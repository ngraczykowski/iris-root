package com.silenteight.scb.ingest.adapter.incomming.common.batch

import com.silenteight.scb.ingest.adapter.incomming.cbs.gateway.CbsAckGateway
import com.silenteight.scb.ingest.adapter.incomming.common.batch.RecordCompositeWriter.WriterConfiguration
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
    def learningModeFlag = true
    def writerConfiguration = new WriterConfiguration(useDelta, ackRecords, learningModeFlag)

    when:
    createRecordWriter(writerConfiguration).write([])

    then:
    1 * ingestService.ingestAlertsForLearn(_ as Stream)
    deltaInteractions * deltaService.updateDelta(_ as Map, deltaJobName)
    ackInteractions * cbsAckGateway.ackReadAlerts(_ as Set)

    where:
    useDelta | ackRecords | deltaInteractions | ackInteractions
    false    | false      | 0                 | 0
    false    | true       | 0                 | 1
    true     | true       | 1                 | 1
    true     | false      | 1                 | 0
  }

  @Unroll
  def "should write alerts for recommendation, useDelta=#useDelta, ackRecords=#ackRecords"() {
    given:
    def learningModeFlag = false
    def writerConfiguration = new WriterConfiguration(useDelta, ackRecords, learningModeFlag)

    when:
    createRecordWriter(writerConfiguration).write([])

    then:
    1 * ingestService.ingestAlertsForRecommendation(_ as Stream)
    deltaInteractions * deltaService.updateDelta(_ as Map, deltaJobName)
    ackInteractions * cbsAckGateway.ackReadAlerts(_ as Set)

    where:
    useDelta | ackRecords | deltaInteractions | ackInteractions
    false    | false      | 0                 | 0
    false    | true       | 0                 | 1
    true     | true       | 1                 | 1
    true     | false      | 1                 | 0
  }

  def 'should do not pass duplicated cbs ack alerts to cbsGateway'() {
    given:
    def writerConfiguration = new WriterConfiguration(false, true, false)
    def alerts = [createAlertComposite(), createAlertComposite()]

    when:
    createRecordWriter(writerConfiguration).write(alerts)

    then:
    1 * cbsAckGateway.ackReadAlerts({it.size() == 1} as Set)
  }

  def 'should merge duplicated alerts for delta update'() {
    given:
    def underTest = createRecordWriter(new WriterConfiguration(true, false, true))
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

  def createRecordWriter(WriterConfiguration configuration) {
    new RecordCompositeWriter(
        deltaService, ingestService, cbsAckGateway, configuration, deltaJobName)
  }
}
