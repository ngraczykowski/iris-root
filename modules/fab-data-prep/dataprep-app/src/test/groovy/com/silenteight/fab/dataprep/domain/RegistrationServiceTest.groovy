package com.silenteight.fab.dataprep.domain

import com.silenteight.fab.dataprep.domain.model.AlertErrorDescription
import com.silenteight.fab.dataprep.domain.model.ParsedAlertMessage
import com.silenteight.fab.dataprep.domain.model.RegisteredAlert
import com.silenteight.registration.api.library.v1.*

import spock.lang.Specification
import spock.lang.Subject

import static com.silenteight.fab.dataprep.domain.Fixtures.*
import static com.silenteight.fab.dataprep.domain.model.AlertStatus.FAILURE
import static com.silenteight.fab.dataprep.domain.model.AlertStatus.SUCCESS

class RegistrationServiceTest extends Specification {

  RegistrationServiceClient registrationServiceClient = Mock()

  @Subject
  def underTest = new RegistrationService(
      registrationServiceClient
  )

  def "registered alerts should have name"() {
    given:
    def alerts = [(MESSAGE_NAME): ParsedAlertMessage.builder()
        .batchName(BATCH_NAME)
        .messageName(MESSAGE_NAME)
        .systemId(SYSTEM_ID)
        .parsedMessageData(PARSED_PAYLOAD)
        .hits(PARSED_ALERT_MESSAGE.hits)
        .build()
    ]
    def request = RegisterAlertsAndMatchesIn.builder()
        .batchId(BATCH_NAME)
        .alertsWithMatches(
            [AlertWithMatchesIn.builder()
                 .alertId(MESSAGE_NAME)
                 .status(AlertStatusIn.SUCCESS)
                 .metadata(METADATA)
                 .matches(
                     [MatchIn.builder()
                          .matchId(HIT_ID)
                          .build()])
                 .build()])
        .build()
    def response = RegisterAlertsAndMatchesOut.builder()
        .registeredAlertWithMatches(
            [
                RegisteredAlertWithMatchesOut.builder()
                    .alertId(MESSAGE_NAME)
                    .alertName(ALERT_NAME)
                    .alertStatus(AlertStatusOut.SUCCESS)
                    .registeredMatches(
                        [
                            RegisteredMatchOut.builder()
                                .matchId(HIT_ID)
                                .matchName(MATCH_NAME)
                                .build()
                        ]
                    )
                    .build()
            ]).build()

    when:
    List<RegisteredAlert> registeredAlerts = underTest.registerAlertsAndMatches(alerts)

    then:
    registeredAlerts.each {
      assert it.getAlertName() == ALERT_NAME
      assert it.getBatchName() == BATCH_NAME
      assert it.getSystemId() == SYSTEM_ID
      assert it.getStatus() == SUCCESS
      it.matches.each {assert it.getMatchName() == MATCH_NAME}
    }
    1 * registrationServiceClient.registerAlertsAndMatches(request) >> response
  }

  def "failed alert should have correct status"() {
    given:
    def alerts = [MESSAGE_NAME]
    def request = RegisterAlertsAndMatchesIn.builder()
        .batchId(BATCH_NAME)
        .alertsWithMatches(
            [AlertWithMatchesIn.builder()
                 .alertId(MESSAGE_NAME)
                 .status(AlertStatusIn.FAILURE)
                 .errorDescription(AlertErrorDescription.EXTRACTION.getDescription())
                 .matches([])
                 .build()])
        .build()
    def response = RegisterAlertsAndMatchesOut.builder()
        .registeredAlertWithMatches(
            [
                RegisteredAlertWithMatchesOut.builder()
                    .alertId(MESSAGE_NAME)
                    .alertName(ALERT_NAME)
                    .alertStatus(AlertStatusOut.FAILURE)
                    .registeredMatches([])
                    .build()
            ]).build()

    when:
    List<RegisteredAlert> registeredAlerts = underTest
        .registerFailedAlerts(alerts, BATCH_NAME, AlertErrorDescription.EXTRACTION)

    then:
    registeredAlerts.each {
      assert it.getAlertName() == ALERT_NAME
      assert it.getBatchName() == BATCH_NAME
      assert it.getStatus() == FAILURE
      assert it.getMatches() == []
    }
    1 * registrationServiceClient.registerAlertsAndMatches(request) >> response
  }
}
