package com.silenteight.scb.ingest.adapter.incomming.common.store.rawalert

import spock.lang.Specification

import java.time.Duration
import java.time.OffsetDateTime

class RawAlertsDataRetentionJobSpec extends Specification {

  def expiredAfter = Duration.ofDays(180)
  def retentionProperties = new RawAlertsDataRetentionProperties(expiredAfter)
  def rawAlertRepository = Mock(RawAlertRepository)
  def underTest = new RawAlertsDataRetentionJob(retentionProperties, rawAlertRepository)

  def "should call method removeExpiredPartitions with proper expired date"() {
    when:
    underTest.removeExpiredAlerts()

    then:
    1 * rawAlertRepository.removeExpiredPartitions(
        {OffsetDateTime expiredDate ->
          expiredDate <= (OffsetDateTime.now() - expiredAfter)
        })
  }
}
