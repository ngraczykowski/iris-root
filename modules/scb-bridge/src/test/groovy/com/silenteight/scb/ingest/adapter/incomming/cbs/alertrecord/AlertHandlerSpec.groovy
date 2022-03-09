package com.silenteight.scb.ingest.adapter.incomming.cbs.alertrecord

import com.silenteight.scb.ingest.adapter.incomming.cbs.alertid.AlertId
import com.silenteight.scb.ingest.adapter.incomming.cbs.alertrecord.InvalidAlert.Reason
import com.silenteight.scb.ingest.adapter.incomming.cbs.alertunderprocessing.AlertInFlightService
import com.silenteight.scb.ingest.adapter.incomming.cbs.alertunderprocessing.AlertUnderProcessing.State
import com.silenteight.scb.ingest.adapter.incomming.cbs.gateway.CbsAckAlert
import com.silenteight.scb.ingest.adapter.incomming.cbs.gateway.CbsAckGateway
import com.silenteight.scb.ingest.adapter.incomming.cbs.gateway.CbsOutput
import com.silenteight.proto.serp.scb.v1.ScbAlertIdContext
import com.silenteight.proto.serp.v1.alert.Alert
import com.silenteight.proto.serp.v1.common.ObjectId
import com.silenteight.scb.ingest.adapter.incomming.common.recommendation.alertinfo.AlertInfoService
import com.silenteight.sep.base.common.messaging.MessageSender
import com.silenteight.sep.base.common.messaging.properties.MessagePropertiesProvider

import spock.lang.Specification

class AlertHandlerSpec extends Specification {

  def alertInfoService = Mock(AlertInfoService)
  def alertInFlightService = Mock(AlertInFlightService)
  def cbsAckGateway = Mock(CbsAckGateway)
  def messageSender = Mock(MessageSender)
  def fixtures = new Fixtures()

  def underTest = new AlertHandler(
      alertInfoService, alertInFlightService, cbsAckGateway, messageSender)

  def 'should handle invalid alerts'() {
    given:
    def watchlistLevel = true
    def alertIdContext = ScbAlertIdContext.newBuilder().setWatchlistLevel(watchlistLevel).build()
    def invalidAlerts = [
        fixtures.invalidAlertCausedByFatalError, fixtures.invalidAlertCausedByTemporaryError
    ]

    when:
    underTest.handleAlerts(alertIdContext, new AlertCompositeCollection([], invalidAlerts))

    then:
    1 * cbsAckGateway.ackReadAlert(_ as CbsAckAlert)
    1 * alertInFlightService.
        update(
            fixtures.invalidAlertCausedByFatalError.alertId, State.ERROR,
            fixtures.invalidAlertCausedByFatalError.getReasonMessage())
  }

  def 'should handle valid alerts'() {
    given:
    def watchlistLevel = true
    def alertIdContext = ScbAlertIdContext.newBuilder().setWatchlistLevel(watchlistLevel).build()
    def validAlerts = [
        fixtures.validAlertComposite1, fixtures.validAlertComposite2
    ]

    when:
    underTest.handleAlerts(alertIdContext, new AlertCompositeCollection(validAlerts, []))

    then:
    1 * cbsAckGateway.
        ackReadAlert({CbsAckAlert a -> a.alertExternalId == fixtures.alertId1.systemId}) >>
        new CbsOutput(state: CbsOutput.State.OK)
    1 * cbsAckGateway.
        ackReadAlert({CbsAckAlert a -> a.alertExternalId == fixtures.alertId2.systemId}) >>
        new CbsOutput(state: CbsOutput.State.ERROR)

    1 * alertInFlightService.delete(fixtures.alertId1)
    1 * alertInFlightService.update(fixtures.alertId2, State.ERROR, "Fatal error on ACK")

    1 * messageSender.send(fixtures.alert1, _ as MessagePropertiesProvider)
    1 * alertInfoService.sendAlertInfo(fixtures.alert1)

    // do not send alert messages when there was a ACK failure
    0 * messageSender.send(fixtures.alert2, _ as MessagePropertiesProvider)
    0 * alertInfoService.sendAlertInfo(fixtures.alert2)
  }

  class Fixtures {

    String someId1 = 'testId-1'
    String someId2 = 'testId-2'

    InvalidAlert invalidAlertCausedByFatalError = new InvalidAlert(someId1, someId1, Reason.ABSENT)
    InvalidAlert invalidAlertCausedByTemporaryError = new InvalidAlert(
        someId2, someId2, Reason.TEMPORARILY_UNAVAILABLE)

    AlertId alertId1 = new AlertId(someId1, someId2)
    AlertId alertId2 = new AlertId(someId2, someId1)

    Alert alert1 = Alert.newBuilder().setId(ObjectId.newBuilder().build()).build()
    Alert alert2 = Alert.newBuilder().setId(ObjectId.newBuilder().build()).build()

    ValidAlertComposite validAlertComposite1 = new ValidAlertComposite(alertId1, [alert1])
    ValidAlertComposite validAlertComposite2 = new ValidAlertComposite(alertId2, [alert2])
  }
}
