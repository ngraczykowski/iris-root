package com.silenteight.bridge.core.recommendation.adapter.outgoing.crossmodule

import com.silenteight.bridge.core.recommendation.domain.FixturesMatchMetaData
import com.silenteight.bridge.core.recommendation.domain.model.BatchWithAlertsDto
import com.silenteight.bridge.core.registration.adapter.outgoing.jdbc.AlertWithoutMatches
import com.silenteight.bridge.core.registration.adapter.outgoing.jdbc.MatchWithAlertId
import com.silenteight.bridge.core.registration.domain.RegistrationFacade
import com.silenteight.bridge.core.registration.domain.command.GetAlertsWithoutMatchesCommand
import com.silenteight.bridge.core.registration.domain.command.GetBatchIdCommand
import com.silenteight.bridge.core.registration.domain.command.GetBatchWithAlertsCommand
import com.silenteight.bridge.core.registration.domain.command.GetMatchesWithAlertIdCommand
import com.silenteight.bridge.core.registration.domain.model.AlertStatus
import com.silenteight.bridge.core.registration.domain.model.AlertWithMatches
import com.silenteight.bridge.core.registration.domain.model.AlertWithMatches.Match
import com.silenteight.bridge.core.registration.domain.model.BatchIdWithPolicy
import com.silenteight.bridge.core.registration.domain.model.BatchWithAlerts

import spock.lang.Specification
import spock.lang.Subject

import java.util.stream.Stream

class RegistrationServiceAdapterSpec extends Specification {

  def registrationFacade = Mock(RegistrationFacade)

  @Subject
  def underTest = new RegistrationServiceAdapter(registrationFacade)

  def 'should get batch with alerts'() {
    given:
    var command = Fixtures.GET_BATCH_WITH_ALERTS_COMMAND

    when:
    def response = underTest.getBatchWithAlerts(Fixtures.ANALYSIS_NAME, command.alertNames())

    then:
    1 * registrationFacade.getBatchWithAlerts(command) >> Fixtures.BATCH_WITH_ALERTS

    with(response) {
      response.batchId == Fixtures.BATCH_ID
      response.policyId == Fixtures.POLICY_NAME
      with(response.alerts().first()) {
        id == Fixtures.ALERT_ID
        name == Fixtures.ALERT_NAME
        status == BatchWithAlertsDto.AlertStatus.PROCESSING
        metadata == Fixtures.METADATA
        errorDescription == null
        with(matches.first()) {
          id == FixturesMatchMetaData.FIRST_METADATA_MATCH_ID
          name == FixturesMatchMetaData.FIRST_METADATA_MATCH_NAME
        }
      }
    }
  }

  def 'should return batch id with policy name'() {
    given:
    var command = Fixtures.GET_BATCH_ID_COMMAND

    when:
    def response = underTest.getBatchId(Fixtures.ANALYSIS_NAME)

    then:
    1 * registrationFacade.getBatchId(command) >> Fixtures.BATCH_ID_WITH_POLICY

    response.id() == Fixtures.BATCH_ID
    response.policyId() == Fixtures.POLICY_NAME
  }

  def 'stream alerts without matches'() {
    given:
    var command = Fixtures.GET_ALERT_WITHOUT_MATCHES_COMMAND

    when:
    def response = underTest.streamAllAlerts(Fixtures.BATCH_ID, command.alertsName())

    then:
    1 * registrationFacade.streamAllByBatchId(command) >> Fixtures.ALERTS_WITHOUT_MATCHES

    def responseList = response.toList()
    responseList.size() == 1
    responseList.get(0).id() == Fixtures.ALERT_ID
  }

  def 'should return matches for alerts'() {
    given:
    var command = Fixtures.GET_MATCHES_COMMAND

    when:
    def response = underTest.getAllMatchesForAlerts(Set.of(Fixtures.ALERT_LONG_ID))

    then:
    1 * registrationFacade.getAllMatchesForAlerts(command) >> Fixtures.MATCHES

    response.size() == 1
    response.get(0).name() == FixturesMatchMetaData.FIRST_METADATA_MATCH_NAME
    response.get(0).id() == FixturesMatchMetaData.FIRST_METADATA_MATCH_ID
  }

  static class Fixtures {

    static def BATCH_ID = 'batchId'
    static def METADATA = 'metadata'
    static def ALERT_LONG_ID = 1L
    static def ALERT_ID = 'alertId'
    static def ALERT_NAME = 'alertName'
    static def POLICY_NAME = 'policyName'
    static def ANALYSIS_NAME = 'analysisName'
    static def GET_BATCH_WITH_ALERTS_COMMAND = new GetBatchWithAlertsCommand(ANALYSIS_NAME, List.of())
    static def GET_BATCH_ID_COMMAND = new GetBatchIdCommand(ANALYSIS_NAME)
    static def GET_ALERT_WITHOUT_MATCHES_COMMAND = new GetAlertsWithoutMatchesCommand(BATCH_ID, List.of())
    static def GET_MATCHES_COMMAND = new GetMatchesWithAlertIdCommand(Set.of(ALERT_LONG_ID))

    static def ALERTS = [
        AlertWithMatches.builder()
            .id(ALERT_ID)
            .name(ALERT_NAME)
            .status(AlertStatus.PROCESSING)
            .metadata(METADATA)
            .matches(
                [
                    new Match(
                        FixturesMatchMetaData.FIRST_METADATA_MATCH_ID,
                        FixturesMatchMetaData.FIRST_METADATA_MATCH_NAME)
                ])
            .build()
    ]

    static def ALERTS_WITHOUT_MATCHES = Stream.of(
        AlertWithoutMatches.builder()
            .id(ALERT_ID)
            .alertName(ALERT_NAME)
            .alertStatus(AlertWithoutMatches.AlertStatus.RECOMMENDED)
            .metadata(METADATA)
            .build()
    )

    static def MATCHES = [
        MatchWithAlertId.builder()
            .alertId(ALERT_ID)
            .id(FixturesMatchMetaData.FIRST_METADATA_MATCH_ID)
            .name(FixturesMatchMetaData.FIRST_METADATA_MATCH_NAME)
            .build()
    ]

    static def BATCH_WITH_ALERTS = new BatchWithAlerts(Fixtures.BATCH_ID, POLICY_NAME, ALERTS)
    static def BATCH_ID_WITH_POLICY = new BatchIdWithPolicy(BATCH_ID, POLICY_NAME)
  }
}
