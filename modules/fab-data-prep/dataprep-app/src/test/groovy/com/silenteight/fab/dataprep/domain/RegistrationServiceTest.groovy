package com.silenteight.fab.dataprep.domain

import com.silenteight.fab.dataprep.domain.model.ParsedAlertMessage
import com.silenteight.fab.dataprep.domain.model.ParsedAlertMessage.Hit
import com.silenteight.fab.dataprep.domain.model.RegisteredAlert
import com.silenteight.registration.api.library.v1.RegisterAlertsAndMatchesOut
import com.silenteight.registration.api.library.v1.RegisteredAlertWithMatchesOut
import com.silenteight.registration.api.library.v1.RegisteredMatchOut
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
    def alerts = ['alertId': ParsedAlertMessage.builder()
        .batchName('batchId')
        .messageName('alertId')
        .systemId('systemId')
        .hits([
            'matchId': Hit.builder()
                .hitName('matchId')
                .build()
        ])
        .build()
    ]
    def response = RegisterAlertsAndMatchesOut.builder()
        .registeredAlertWithMatches(
            [
                RegisteredAlertWithMatchesOut.builder()
                    .alertId('alertId')
                    .alertName('alertName')
                    .registeredMatches(
                        [
                            RegisteredMatchOut.builder()
                                .matchId('matchId')
                                .matchName('matchName')
                                .build()
                        ]
                    )
                    .build()
            ]).build()

    when:
    List<RegisteredAlert> registeredAlerts = underTest.registerAlertsAndMatches(alerts);

    then:
    registeredAlerts.each {
      assert it.getAlertName() == 'alertName'
      assert it.getSystemId() == 'systemId'
      it.matches.each {assert it.getMatchName() == 'matchName'}
    }
    1 * registrationServiceClient.registerAlertsAndMatches(_) >> response
  }

}