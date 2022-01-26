package com.silenteight.bridge.core.registration.adapter.incoming

import com.silenteight.bridge.core.registration.domain.RegisterAlertsCommand
import com.silenteight.bridge.core.registration.domain.model.RegistrationAlert
import com.silenteight.bridge.core.registration.domain.model.RegistrationAlert.RegistrationMatch
import com.silenteight.bridge.core.registration.domain.model.RegistrationAlert.Status
import com.silenteight.proto.registration.api.v1.AlertStatus
import com.silenteight.proto.registration.api.v1.AlertWithMatches
import com.silenteight.proto.registration.api.v1.Match
import com.silenteight.proto.registration.api.v1.RegisterAlertsAndMatchesRequest

import spock.lang.Specification
import spock.lang.Subject

class RegistrationGrpcMapperSpec extends Specification {

  @Subject
  def underTest = new RegistrationGrpcMapper()

  void 'should map request to register alerts command'() {
    given:
    def matchesIn = [
        Match.newBuilder()
            .setMatchId('matchId')
            .build()
    ]

    def alertsWithMatches = [
        AlertWithMatches.newBuilder()
            .setAlertId('alertId')
            .setStatus(AlertStatus.SUCCESS)
            .setErrorDescription("")
            .addAllMatches(matchesIn)
            .build()
    ]

    def request = RegisterAlertsAndMatchesRequest.newBuilder()
        .setBatchId('batchId')
        .addAllAlertsWithMatches(alertsWithMatches)
        .build()

    when:
    def result = underTest.toRegisterAlertsCommand(request)

    then:
    with(result) {
      batchId() == 'batchId'
      with(alertWithMatches().first()) {
        alertId() == 'alertId'
        alertStatus() == RegisterAlertsCommand.AlertStatus.SUCCESS
        errorDescription().isEmpty()
        matches().first().id() == 'matchId'
      }
    }
  }

  void 'should map alerts to register alerts and matches response'() {
    given:
    def alerts = [
        RegistrationAlert.builder()
            .id('alertId')
            .name('alertName')
            .status(Status.SUCCESS)
            .matches(
                [
                    RegistrationMatch.builder()
                        .id('matchId')
                        .name('matchName')
                        .build()
                ]
            )
            .build()
    ]

    when:
    def result = underTest.toRegisterAlertsAndMatchesResponse(alerts)

    then:
    with(result.registeredAlertsWithMatches_.first()) {
      alertId == 'alertId'
      alertName == 'alertName'
      alertStatus == AlertStatus.SUCCESS
      with(registeredMatches_.first()) {
        matchId == 'matchId'
        matchName == 'matchName'
      }
    }
  }
}
