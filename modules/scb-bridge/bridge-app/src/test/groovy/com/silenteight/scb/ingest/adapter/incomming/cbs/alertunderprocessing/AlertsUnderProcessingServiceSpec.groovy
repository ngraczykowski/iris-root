package com.silenteight.scb.ingest.adapter.incomming.cbs.alertunderprocessing

import com.silenteight.scb.ingest.adapter.incomming.cbs.alertid.AlertId
import com.silenteight.sep.base.testing.transaction.CleanTransactionManagerCacheExtension

import spock.lang.Specification

import java.time.OffsetDateTime

class AlertsUnderProcessingServiceSpec extends Specification {

  def repository = Mock(AlertUnderProcessingRepository)
  def objectUnderTest = new AlertsUnderProcessingService(repository)
  def cleanTransactionManagerCacheExtension = new CleanTransactionManagerCacheExtension()

  def setup() {
    cleanTransactionManagerCacheExtension.cleanTransactionManagerCache()
  }

  def 'should delete alert under processing'() {
    given:
    def alertId = new AlertId('systemId', 'batchId')

    when:
    objectUnderTest.delete(alertId)

    then:
    1 * repository.deleteBySystemIdAndBatchId(alertId.systemId, alertId.batchId)
  }

  def 'should delete expired records'() {
    def expireDate = OffsetDateTime.now()

    when:
    objectUnderTest.deleteExpired(expireDate)

    then:
    1 * repository.deleteByCreatedAtBefore(expireDate)
  }
}
