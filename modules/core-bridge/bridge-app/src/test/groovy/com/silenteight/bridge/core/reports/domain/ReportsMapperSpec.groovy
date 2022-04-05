package com.silenteight.bridge.core.reports.domain

import com.silenteight.bridge.core.reports.ReportFixtures

import spock.lang.Specification
import spock.lang.Subject

class ReportsMapperSpec extends Specification {

  @Subject
  def underTest = new ReportsMapper()

  def 'should map alerts and recommendations into a report'() {
    given:
    def batchId = ReportFixtures.BATCH_ID
    def alertWithMatches = ReportFixtures.ALERT_ONE
    def recommendation = ReportFixtures.FIRST_RECOMMENDATION_WITH_METADATA

    when:
    def report = underTest.toReport(batchId, alertWithMatches, recommendation)

    then:
    report == ReportFixtures.REPORT_ONE
  }
}
