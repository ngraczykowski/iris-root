package com.silenteight.fab.dataprep.domain

import com.silenteight.fab.dataprep.domain.model.ExtractedAlert
import com.silenteight.fab.dataprep.domain.model.ExtractedAlert.Match
import com.silenteight.registration.api.library.v1.RegisterAlertsAndMatchesOut
import com.silenteight.registration.api.library.v1.RegisteredAlertWithMatchesOut
import com.silenteight.registration.api.library.v1.RegistrationServiceClient

import spock.lang.Specification
import spock.lang.Subject

class RegistrationServiceTest extends Specification {

  RegistrationServiceClient registrationServiceClient = Mock()

  @Subject
  def underTest = new RegistrationService(
      registrationServiceClient
  )

  def "registered alerts should have name"() {
    given:
    def alerts = List.of(
        ExtractedAlert.builder()
            .batchId('batchId')
            .alertId('alertId')
            .matches(
                [
                    Match.builder()
                        .matchId('matchId')
                        .build()
                ]
            )
            .build())
    def response = RegisterAlertsAndMatchesOut.builder()
        .registeredAlertWithMatches(
            [
                RegisteredAlertWithMatchesOut.builder()
                    .alertId('alertId')
                    .alertName('alertName')
                    .build()
            ]).build()

    when:
    underTest.registerAlertsAndMatches(alerts);

    then:
    alerts.each {it.getAlertName() == 'alertName'}
    1 * registrationServiceClient.registerAlertsAndMatches(_) >> response
  }

}
