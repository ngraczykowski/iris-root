package com.silenteight.bridge.core.recommendation.adapter.outgoing.crossmodule

import com.silenteight.bridge.core.recommendation.domain.FixturesMatchMetaData
import com.silenteight.bridge.core.recommendation.domain.model.BatchWithAlertsDto
import com.silenteight.bridge.core.registration.domain.RegistrationFacade
import com.silenteight.bridge.core.registration.domain.command.GetBatchWithAlertsCommand
import com.silenteight.bridge.core.registration.domain.model.AlertStatus
import com.silenteight.bridge.core.registration.domain.model.AlertWithMatches
import com.silenteight.bridge.core.registration.domain.model.AlertWithMatches.Match
import com.silenteight.bridge.core.registration.domain.model.BatchWithAlerts

import spock.lang.Specification
import spock.lang.Subject

class RegistrationServiceAdapterSpec extends Specification {

  def registrationFacade = Mock(RegistrationFacade)

  @Subject
  def underTest = new RegistrationServiceAdapter(registrationFacade)

  def 'should get batch with alerts'() {
    given:
    var command = Fixtures.GET_BATCH_WITH_ALERTS_COMMAND

    when:
    def response = underTest.getBatchWithAlerts(Fixtures.ANALYSIS_NAME)

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

  static class Fixtures {

    static def BATCH_ID = 'batchId'
    static def METADATA = 'metadata'
    static def ALERT_ID = 'alertId'
    static def ALERT_NAME = 'alertName'
    static def POLICY_NAME = 'policyName'
    static def ANALYSIS_NAME = 'analysisName'
    static def GET_BATCH_WITH_ALERTS_COMMAND = new GetBatchWithAlertsCommand(ANALYSIS_NAME)

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

    static def BATCH_WITH_ALERTS = new BatchWithAlerts(Fixtures.BATCH_ID, POLICY_NAME, ALERTS)
  }
}
