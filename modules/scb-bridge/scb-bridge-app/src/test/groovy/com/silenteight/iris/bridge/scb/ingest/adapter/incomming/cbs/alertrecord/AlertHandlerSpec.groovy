/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.alertrecord

import com.silenteight.proto.serp.scb.v1.ScbAlertIdContext
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.alertid.AlertId
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.alertmapper.AlertMapper
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.alertrecord.InvalidAlert.Reason
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.alertunderprocessing.AlertInFlightService
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.gateway.CbsAckAlert
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.gateway.CbsAckGateway
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.gateway.CbsOutput
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.gateway.CbsOutput.State
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.ingest.BatchAlertIngestService
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.ingest.IngestedAlertsStatus
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.ObjectId
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.util.InternalBatchIdGenerator
import com.silenteight.iris.bridge.scb.ingest.domain.model.RegistrationBatchContext

import spock.lang.Specification

class AlertHandlerSpec extends Specification {

  def alertInFlightService = Mock(AlertInFlightService)
  def cbsAckGateway = Mock(CbsAckGateway)
  def alertMapper = Mock(AlertMapper)
  def ingestService = Mock(BatchAlertIngestService)
  def rawAlertService = Mock(com
      .silenteight.iris.bridge.scb.ingest.adapter.incomming.common.store.rawalert.RawAlertService)
  def fixtures = new Fixtures()

  def underTest = AlertHandler.builder()
      .alertInFlightService(alertInFlightService)
      .cbsAckGateway(cbsAckGateway)
      .alertMapper(alertMapper)
      .ingestService(ingestService)
      .rawAlertService(rawAlertService)
      .build()

  def 'should handle invalid alerts'() {
    given:
    def internalBatchId = InternalBatchIdGenerator.generate()
    def alertIdContext = ScbAlertIdContext.newBuilder().setWatchlistLevel(true).build()
    def invalidAlerts = [
        fixtures.invalidAlertCausedByFatalError, fixtures.invalidAlertCausedByTemporaryError
    ]
    def alertCompositeCollection = new AlertCompositeCollection([], invalidAlerts)

    when:
    underTest
        .handleAlerts(internalBatchId, alertIdContext, alertCompositeCollection)

    then:
    1 * alertMapper.fromValidAlertComposites([]) >> []
    1 * cbsAckGateway.ackReadAlert(_ as CbsAckAlert)
    1 * alertInFlightService.error(
        fixtures.invalidAlertCausedByFatalError.alertId,
        fixtures.invalidAlertCausedByFatalError.getReasonMessage())
    0 * rawAlertService.store(_, _)
  }

  def 'should handle valid alerts'() {
    given:
    def internalBatchId = InternalBatchIdGenerator.generate()
    def alertIdContext = ScbAlertIdContext.newBuilder().setWatchlistLevel(true).build()
    def batchContext = RegistrationBatchContext.CBS_CONTEXT
    def alerts = [fixtures.alert1, fixtures.alert2]
    def validAlertComposites = [
        fixtures.validAlertComposite1, fixtures.validAlertComposite2
    ]
    def alertCompositeCollection = new AlertCompositeCollection(validAlertComposites, [])

    when:
    underTest
        .handleAlerts(internalBatchId, alertIdContext, alertCompositeCollection)

    then:
    1 * alertMapper.fromValidAlertComposites(validAlertComposites) >> alerts
    1 * ingestService.ingestAlertsForRecommendation(internalBatchId, alerts, batchContext) >>
        new IngestedAlertsStatus(alerts, [])
    1 * cbsAckGateway.
        ackReadAlert({CbsAckAlert a -> a.alertExternalId == fixtures.alertId1.systemId}) >>
        new CbsOutput(state: State.OK)
    1 * cbsAckGateway.
        ackReadAlert({CbsAckAlert a -> a.alertExternalId == fixtures.alertId2.systemId}) >>
        new CbsOutput(state: State.ERROR)

    1 * alertInFlightService.ack(fixtures.alertId1)
    1 * alertInFlightService.error(fixtures.alertId2, "Fatal error on ACK")
    1 * rawAlertService.store(internalBatchId, [fixtures.alert1, fixtures.alert2])
  }

  class Fixtures {

    String someId1 = 'testId-1'
    String someId2 = 'testId-2'

    InvalidAlert invalidAlertCausedByFatalError = new InvalidAlert(someId1, someId1, Reason.ABSENT)
    InvalidAlert invalidAlertCausedByTemporaryError = new InvalidAlert(
        someId2, someId2, Reason.TEMPORARILY_UNAVAILABLE)

    AlertId alertId1 = new AlertId(someId1, someId2)
    AlertId alertId2 = new AlertId(someId2, someId1)

    com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.alert.Alert alert1 = com
        .silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.alert.Alert.builder()
        .id(ObjectId.builder().build())
        .details(
            com
                .silenteight
                .iris.bridge.scb.ingest.adapter.incomming.common.model.alert.AlertDetails.builder()
                .systemId(someId1)
                .batchId(someId2)
                .build())
        .build()
    com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.alert.Alert alert2 = com
        .silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.alert.Alert.builder()
        .id(ObjectId.builder().build())
        .details(
            com
                .silenteight
                .iris.bridge.scb.ingest.adapter.incomming.common.model.alert.AlertDetails.builder()
                .systemId(someId2)
                .batchId(someId1)
                .build())
        .build()

    ValidAlertComposite validAlertComposite1 = new ValidAlertComposite(alertId1, [alert1])
    ValidAlertComposite validAlertComposite2 = new ValidAlertComposite(alertId2, [alert2])
  }
}
