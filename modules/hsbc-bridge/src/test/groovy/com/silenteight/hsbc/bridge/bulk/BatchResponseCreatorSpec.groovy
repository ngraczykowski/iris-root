package com.silenteight.hsbc.bridge.bulk

import com.silenteight.hsbc.bridge.alert.AlertStatus
import com.silenteight.hsbc.bridge.bulk.rest.BatchAlertItemStatus
import com.silenteight.hsbc.bridge.bulk.rest.BatchStatus

import spock.lang.Specification

class BatchResponseCreatorSpec extends Specification {

  def 'should create Batch Response from batch data'() {
    given:
    def bulk = new Bulk(
        alerts: [
            new BulkAlertEntity(
                errorMessage: 'error message',
                externalId: 'alert-1',
                status: AlertStatus.PRE_PROCESSED
            )
        ],
        analysis: new BulkAnalysisEntity(
            policy: 'policyName'
        ),
        id: 'id',
        status: BulkStatus.PRE_PROCESSED
    )

    when:
    def result = BatchResponseCreator.create(bulk)

    then:
    with(result) {
      batchId == bulk.id
      batchStatus == BatchStatus.PROCESSING
      policyName == 'policyName'
      requestedAlerts.size() == 1
      with(requestedAlerts.first()) {
        errorMessage == 'error message'
        id == 'alert-1'
        status == BatchAlertItemStatus.PROCESSING
      }
    }
  }
}
