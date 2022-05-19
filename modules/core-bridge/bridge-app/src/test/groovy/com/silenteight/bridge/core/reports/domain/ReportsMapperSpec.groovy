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

  def 'should map alert and matches to AlertWithMatchesDto'() {
    given:
    def alertWithoutMatches = ReportFixtures.ALERT_ONE_WITHOUT_MATCHES
    def matches = List.of(ReportFixtures.MATCH_ONE_WITH_ALERT_ID, ReportFixtures.MATCH_TWO_WITH_ALERT_ID)

    when:
    def result = underTest.toAlertWithMatches(alertWithoutMatches, matches)

    then:
    result == ReportFixtures.ALERT_ONE
  }
}
