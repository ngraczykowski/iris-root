package com.silenteight.scb.ingest.adapter.incomming.amqp

import com.silenteight.dataretention.api.v1.AlertData
import com.silenteight.dataretention.api.v1.AlertsExpired
import com.silenteight.scb.ingest.domain.DataRetentionService
import com.silenteight.scb.ingest.domain.model.DataRetentionCommand
import com.silenteight.scb.ingest.domain.model.DataRetentionCommand.DataRetentionAlert

import spock.lang.Specification
import spock.lang.Subject

class DataRetentionEventListenerSpec extends Specification {

  def dataRetentionService = Mock(DataRetentionService)

  @Subject
  def underTest = new DataRetentionEventListener(dataRetentionService)

  def 'should call facade with proper command'() {
    given:
    def message = AlertsExpired.newBuilder()
        .addAllAlerts(Fixtures.ALERT_NAMES)
        .addAllAlertsData(Fixtures.ALERTS_DATA)
        .build()

    when:
    underTest.receiveMessage(message)

    then:
    1 * dataRetentionService.performDataRetention(Fixtures.EXPECTED_COMMAND)
  }

  static class Fixtures {

    static def ALERT_NAMES = ['alert1']
    static def ALERTS_DATA = [
        AlertData.newBuilder()
            .setBatchId('batch1')
            .setAlertId('1')
            .setAlertName('alert1')
            .build()
    ]
    static def EXPECTED_COMMAND = new DataRetentionCommand(
        [
            DataRetentionAlert.builder()
                .internalBatchId('batch1')
                .systemId('1')
                .build()
        ])
  }
}
