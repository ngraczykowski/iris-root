package com.silenteight.scb.ingest.adapter.incomming.cbs.alertrecord

import com.silenteight.proto.serp.scb.v1.ScbAlertIdContext
import com.silenteight.scb.ingest.adapter.incomming.cbs.alertid.AlertId
import com.silenteight.scb.ingest.adapter.incomming.cbs.alertmapper.AlertMapper
import com.silenteight.scb.ingest.adapter.incomming.cbs.alertrecord.InvalidAlert.Reason
import com.silenteight.scb.ingest.adapter.incomming.cbs.alertunderprocessing.AlertInFlightService
import com.silenteight.scb.ingest.adapter.incomming.cbs.alertunderprocessing.AlertUnderProcessing.State
import com.silenteight.scb.ingest.adapter.incomming.cbs.gateway.CbsAckAlert
import com.silenteight.scb.ingest.adapter.incomming.cbs.gateway.CbsAckGateway
import com.silenteight.scb.ingest.adapter.incomming.cbs.gateway.CbsOutput
import com.silenteight.scb.ingest.adapter.incomming.common.ingest.BatchAlertIngestService
import com.silenteight.scb.ingest.adapter.incomming.common.model.ObjectId
import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert
import com.silenteight.scb.ingest.adapter.incomming.common.store.rawalert.RawAlertService
import com.silenteight.scb.ingest.adapter.incomming.common.util.InternalBatchIdGenerator
import com.silenteight.scb.ingest.domain.model.RegistrationBatchContext

import spock.lang.Specification

import static com.silenteight.scb.ingest.domain.model.Batch.Priority.MEDIUM
import static com.silenteight.scb.ingest.domain.model.BatchSource.CBS

class AlertHandlerSpec extends Specification {

  def alertInFlightService = Mock(AlertInFlightService)
  def cbsAckGateway = Mock(CbsAckGateway)
  def validAlertCompositeMapper = Mock(ValidAlertCompositeMapper)
  def invalidAlertMapper = Mock(InvalidAlertMapper)
  def alertMapper = Mock(AlertMapper)
  def ingestService = Mock(BatchAlertIngestService)
  def rawAlertService = Mock(RawAlertService)
  def fixtures = new Fixtures()

  def underTest =  AlertHandler.builder()
      .alertInFlightService(alertInFlightService)
      .cbsAckGateway(cbsAckGateway)
      .validAlertCompositeMapper(validAlertCompositeMapper)
      .invalidAlertMapper(invalidAlertMapper)
      .alertMapper(alertMapper)
      .ingestService(ingestService)
      .rawAlertService(rawAlertService)
      .build();

  def 'should handle invalid alerts'() {
    given:
    def internalBatchId = InternalBatchIdGenerator.generate()
    def invalidAlerts = [
        fixtures.invalidAlertCausedByFatalError, fixtures.invalidAlertCausedByTemporaryError
    ]
    def alertCompositeCollections = List.of(new AlertCompositeCollection([], invalidAlerts))

    when:
    underTest
        .handleAlerts(internalBatchId, alertCompositeCollections)

    then:
    1 * validAlertCompositeMapper.fromAlertCompositeCollections(alertCompositeCollections) >>
        Collections.emptyList()
    1 * invalidAlertMapper.fromAlertCompositeCollections(alertCompositeCollections) >>
        invalidAlerts
    1 * cbsAckGateway.ackReadAlert(_ as CbsAckAlert)
    1 * alertInFlightService.
        update(
            fixtures.invalidAlertCausedByFatalError.alertId, State.ERROR,
            fixtures.invalidAlertCausedByFatalError.getReasonMessage())
    0 * rawAlertService.store(_, _)
  }

  def 'should handle valid alerts'() {
    given:
    def internalBatchId = InternalBatchIdGenerator.generate()
    def batchContext = new RegistrationBatchContext(MEDIUM, CBS)
    def alerts = [fixtures.alert1, fixtures.alert2]
    def validAlertComposites = [
        fixtures.validAlertComposite1, fixtures.validAlertComposite2
    ]
    def alertCompositeCollections = List.of(new AlertCompositeCollection(validAlertComposites, []))

    when:
    underTest
        .handleAlerts(internalBatchId, alertCompositeCollections)

    then:
    1 * validAlertCompositeMapper.fromAlertCompositeCollections(alertCompositeCollections) >>
        validAlertComposites
    1 * invalidAlertMapper.fromAlertCompositeCollections(alertCompositeCollections) >>
        Collections.emptyList()
    1 * alertMapper.fromValidAlertComposites(validAlertComposites) >> alerts
    1 * ingestService.ingestAlertsForRecommendation(internalBatchId, alerts, batchContext)
    1 * cbsAckGateway.
        ackReadAlert({CbsAckAlert a -> a.alertExternalId == fixtures.alertId1.systemId}) >>
        new CbsOutput(state: CbsOutput.State.OK)
    1 * cbsAckGateway.
        ackReadAlert({CbsAckAlert a -> a.alertExternalId == fixtures.alertId2.systemId}) >>
        new CbsOutput(state: CbsOutput.State.ERROR)

    1 * alertInFlightService.delete(fixtures.alertId1)
    1 * alertInFlightService.update(fixtures.alertId2, State.ERROR, "Fatal error on ACK")
    1 * rawAlertService.store(internalBatchId, [ fixtures.alert1 ])
    1 * rawAlertService.store(internalBatchId, [ fixtures.alert2 ])
  }

  class Fixtures {

    String someId1 = 'testId-1'
    String someId2 = 'testId-2'

    ScbAlertIdContext alertIdContext = ScbAlertIdContext.newBuilder().setSourceView('test').build()

    InvalidAlert invalidAlertCausedByFatalError = new InvalidAlert(
        someId1, someId1, Reason.ABSENT, alertIdContext)
    InvalidAlert invalidAlertCausedByTemporaryError = new InvalidAlert(
        someId2, someId2, Reason.TEMPORARILY_UNAVAILABLE, alertIdContext)

    AlertId alertId1 = new AlertId(someId1, someId2)
    AlertId alertId2 = new AlertId(someId2, someId1)

    Alert alert1 = Alert.builder().id(ObjectId.builder().build()).build()
    Alert alert2 = Alert.builder().id(ObjectId.builder().build()).build()

    ValidAlertComposite validAlertComposite1 = new ValidAlertComposite(
        alertId1, [alert1], alertIdContext)
    ValidAlertComposite validAlertComposite2 = new ValidAlertComposite(
        alertId2, [alert2], alertIdContext)
  }
}
