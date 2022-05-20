package com.silenteight.hsbc.bridge.bulk

import com.silenteight.hsbc.bridge.alert.WarehouseApi
import com.silenteight.hsbc.bridge.bulk.rest.AlertMetadata
import com.silenteight.hsbc.bridge.bulk.rest.AlertRecommendation
import com.silenteight.hsbc.bridge.bulk.rest.BatchAlertItemStatus
import com.silenteight.hsbc.bridge.bulk.rest.BatchSolvedAlerts
import com.silenteight.hsbc.bridge.report.Alert

import spock.lang.Specification

class IngestRecommendationsUseCaseSpec extends Specification {

  def warehouseApi = Mock(WarehouseApi)
  def underTest = new IngestRecommendationsUseCase(warehouseApi)

  def "should convert solvedAlert to Alert"() {
    given:
    def batchAlerts = new BatchSolvedAlerts(alerts: ALERT_RECOMMENDATION)

    when:
    underTest.ingest(batchAlerts)

    then:
    1 * warehouseApi.send(*_) >> {arguments ->
      Collection<Alert> argument = arguments[0] as Collection<Alert>
      with(argument.first()) {
        discriminator == "someDiscriminator0"
        def metadata = getMetadata()
        metadata.get("trackingId") == "someTrackingId0"
        metadata.get("extendedAttribute5") == "value0"
        getMatches().isEmpty()
      }

      with(argument[1]) {

        def metadata = getMetadata()
        metadata.get("errorMessage") == "someErrorMessage"
        metadata.get("trackingId") == "someTrackingId1"
        metadata.get("extendedAttribute5") == "value1"
        getMatches().isEmpty()
      }
    }
  }

  static List<AlertRecommendation> ALERT_RECOMMENDATION = [
      new AlertRecommendation(
          id: 'someAlertId',
          status: BatchAlertItemStatus.COMPLETED,
          recommendation: 'someRecommendation',
          comment: 'someComment',
          policyId: 'somePolicyId',
          stepId: 'someStepId',
          fvSignature: 'someFvSignature',
          alertMetadata: [
              new AlertMetadata('trackingId', 'someTrackingId0'),
              new AlertMetadata('discriminator', 'someDiscriminator0'),
              new AlertMetadata('extendedAttribute5', 'value0')
          ]
      ),
      new AlertRecommendation(
          id: 'someAlertId1',
          status: BatchAlertItemStatus.ERROR,
          errorMessage: 'someErrorMessage',
          recommendation: 'someRecommendation',
          comment: 'someComment',
          alertMetadata: [
              new AlertMetadata('trackingId', 'someTrackingId1'),
              new AlertMetadata('extendedAttribute5', 'value1')
          ]
      )
  ]
}

