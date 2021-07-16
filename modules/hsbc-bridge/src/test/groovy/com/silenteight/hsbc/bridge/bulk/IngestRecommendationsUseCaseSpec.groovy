package com.silenteight.hsbc.bridge.bulk

import com.silenteight.hsbc.bridge.bulk.rest.AlertMetadata
import com.silenteight.hsbc.bridge.bulk.rest.BatchSolvedAlerts
import com.silenteight.hsbc.bridge.bulk.rest.ErrorAlert
import com.silenteight.hsbc.bridge.bulk.rest.SolvedAlert
import com.silenteight.hsbc.bridge.report.Alert
import com.silenteight.hsbc.bridge.report.Warehouse

import spock.lang.Specification

class IngestRecommendationsUseCaseSpec extends Specification {

  def warehouseApi = Mock(Warehouse)
  def underTest = new IngestRecommendationsUseCase(warehouseApi)

  def "should convert solvedAlert to Alert"() {
    given:
    def batchAlerts = new BatchSolvedAlerts(
        alerts: SOLVED_ALERTS,
        errorAlerts: ERROR_ALERTS
    )

    when:
    underTest.ingest(batchAlerts)

    then:
    1 * warehouseApi.send(*_) >> {arguments ->
      Collection<Alert> argument = arguments[0] as Collection<Alert>
      with(argument.first()) {
        discriminator == "someDiscriminator0"
        def metadata = getMetadata()
        metadata.get("trackingId") == "someTrackingId0"
        metadata.get("extendedAttribute5") == "value3"
        getMatches().isEmpty()
      }

      with(argument[1]) {
        discriminator == "someDiscriminator1"
        def metadata = getMetadata()
        metadata.get("comment") == "someComment"
        metadata.get("trackingId") == "someTrackingId1"
        metadata.get("extendedAttribute5") == "value2"
        getMatches().isEmpty()
      }
    }
  }

  static List<SolvedAlert> ERROR_ALERTS = [
      new ErrorAlert(
          id: 'someAlertId1',
          errorMessage: 'Some error',
          alertMetadata: [
              new AlertMetadata(
                  key: 'trackingId',
                  value: 'someTrackingId0'
              ),
              new AlertMetadata(
                  key: 'discriminator',
                  value: 'someDiscriminator0'
              ),
              new AlertMetadata(
                  key: 'extendedAttribute5',
                  value: 'value3'
              )
          ]
      )
  ]

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
                  key: 'trackingId',
                  value: 'someTrackingId1'
              ),
              new AlertMetadata(
                  key: 'discriminator',
                  value: 'someDiscriminator1'
              ),
              new AlertMetadata(
                  key: 'extendedAttribute5',
                  value: 'value2'
              )
          ]
      )
  ]
}

