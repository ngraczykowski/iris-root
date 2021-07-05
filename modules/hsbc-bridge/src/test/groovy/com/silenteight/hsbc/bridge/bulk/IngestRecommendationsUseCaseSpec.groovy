package com.silenteight.hsbc.bridge.bulk

import com.silenteight.hsbc.bridge.bulk.rest.AlertMetadata
import com.silenteight.hsbc.bridge.bulk.rest.SolvedAlert
import com.silenteight.hsbc.bridge.report.Alert
import com.silenteight.hsbc.bridge.report.Warehouse

import spock.lang.Specification

class IngestRecommendationsUseCaseSpec extends Specification {

  def warehouseApi = Mock(Warehouse)
  def underTest = new IngestRecommendationsUseCase(warehouseApi)

  def "should convert solvedAlert to Alert"() {
    when:
    underTest.ingest(SOLVED_ALERTS)

    then:
    1 * warehouseApi.send(*_) >> {arguments ->
      Collection<Alert> argument = arguments[0] as Collection<Alert>
      assert argument.first().getName() == "someAlertId"
      assert argument.first().getMetadata().get("comment") == "someComment"
      assert argument.first().getMetadata().get("key1") == "value1"
      assert argument.first().getMatches().isEmpty()
    }
  }

  static List<SolvedAlert> SOLVED_ALERTS = [
      new SolvedAlert(
          id: 'someAlertId',
          recommendation: 'someRecommendation',
          comment: 'someComment',
          policyId: 'somePolicyId',
          stepId: 'someStepId',
          fvSignature: 'someFvSignature',
          alertMetadata: [
              new AlertMetadata(
                  key: 'key1',
                  value: 'value1'
              ),
              new AlertMetadata(
                  key: 'key2',
                  value: 'value2'
              )
          ]
      )
  ]
}

