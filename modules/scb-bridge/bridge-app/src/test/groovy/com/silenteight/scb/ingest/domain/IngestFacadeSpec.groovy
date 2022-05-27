package com.silenteight.scb.ingest.domain

import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert
import com.silenteight.scb.ingest.adapter.incomming.common.util.InternalBatchIdGenerator
import com.silenteight.scb.ingest.domain.model.DataRetentionCommand
import com.silenteight.scb.ingest.domain.model.DataRetentionCommand.DataRetentionAlert
import com.silenteight.scb.ingest.domain.model.RegistrationBatchContext
import com.silenteight.scb.ingest.domain.model.RegistrationResponse

import spock.lang.Specification
import spock.lang.Subject

class IngestFacadeSpec extends Specification {

  def batchRegistrationService = Mock(BatchRegistrationService)
  def alertRegistrationService = Mock(AlertRegistrationService)
  def dataRetentionService = Mock(DataRetentionService)

  @Subject
  def underTest = new IngestFacade(
      batchRegistrationService, alertRegistrationService, dataRetentionService)

  def "should call batch and alert registration services"() {
    given:
    def internalBatchId = InternalBatchIdGenerator.generate()
    def alerts = [Alert.builder().build()]
    def context = RegistrationBatchContext.GNS_RT_CONTEXT

    when:
    underTest.registerAlerts(internalBatchId, alerts, context)

    then:
    1 * batchRegistrationService.register(internalBatchId, alerts, context)
    1 * alertRegistrationService.registerAlertsAndMatches(internalBatchId, alerts) >>
        RegistrationResponse.empty()
  }

  def "should call data retention service"() {
    given:
    def command = new DataRetentionCommand([new DataRetentionAlert('systemId', 'internalBatchId')])

    when:
    underTest.performDataRetention(command)

    then:
    1 * dataRetentionService.performDataRetention(command)
  }
}
