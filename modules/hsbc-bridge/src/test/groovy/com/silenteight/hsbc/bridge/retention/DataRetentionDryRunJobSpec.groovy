package com.silenteight.hsbc.bridge.retention

import spock.lang.Specification
import spock.lang.Subject

import java.time.Duration

class DataRetentionDryRunJobSpec extends Specification {

  def dryRunDataCleaner = Mock(DryRunDataCleaner)
  def jobRepository = Mock(DryRunJobRepository)
  def alertRepository = Mock(DryRunJobAlertRepository)

  @Subject
  def underTest = new DataRetentionDryRunJob(dryRunDataCleaner, jobRepository, alertRepository)

  def 'should process dry run'() {
    given:
    1 * jobRepository.save(_) >> 1
    1 * dryRunDataCleaner.getAlertNamesToClean(_) >> ['alert1', 'alert2']

    when:
    underTest.process(Duration.ofDays(1))

    then:
    alertRepository.saveAll(1, ['alert1', 'alert2'] as Set)
  }
}
